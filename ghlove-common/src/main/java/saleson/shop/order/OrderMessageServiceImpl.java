package saleson.shop.order;

import com.onlinepowers.framework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.common.notification.ApplicationInfoService;
import saleson.common.notification.UnifiedMessagingService;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.ShopUtils;
import saleson.model.Ums;
import saleson.model.campaign.ApplicationInfo;
import saleson.shop.config.domain.Config;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.mailconfig.support.OrderMail;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyPayment;
import saleson.shop.order.domain.Buyer;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderSendMessageLog;
import saleson.shop.order.infra.OrderEncryptor;
import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.sendmaillog.domain.SendMailLog;
import saleson.shop.ums.UmsService;
import saleson.shop.ums.support.OrderBank;
import saleson.shop.ums.support.OrderDelivering;
import saleson.shop.ums.support.OrderNew;

@Slf4j
@RequiredArgsConstructor
@Service("orderMessageService")
public class OrderMessageServiceImpl extends EgovAbstractServiceImpl implements OrderMessageService {

    private final OrderMapper orderMapper;
    private final OrderEncryptor orderEncryptor;

    private final MailConfigService mailConfigService;
    private final Cryptor cryptor;
    private final DataMasking dataMasking;
    private final SendMailLogService sendMailLogService;
    private final UmsService umsService;
    private final ApplicationInfoService applicationInfoService;
    private final UnifiedMessagingService unifiedMessagingService;

    @Override
    public void sendOrderMessageTx(Buy buy) {

        if (buy == null) {
            return;
        }

        Config config = ShopUtils.getConfig();
        String templateId = "0".equals(buy.getOrderStatus()) ? "order_deposit_wait" : "order_cready_payment";

        OrderSendMessageLog orderSendMessageLog = new OrderSendMessageLog();
        orderSendMessageLog.setOrderCode(buy.getOrderCode());
        orderSendMessageLog.setTemplateId(templateId);

        // 사용자 발송 체크
        if (orderMapper.getOrderSendMessageLogCount(orderSendMessageLog) == 0) {

            if ("order_cready_payment".equals(templateId)) {
                long userId = buy.getUserId();

                Buyer buyer = buy.getBuyer();
                MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
                if (mailConfig != null) {

                    if (ObjectUtils.isEmpty(buyer.getEmail()) == false) {

                        OrderMail orderMail = new OrderMail(buy, userId, mailConfig, config, cryptor, dataMasking);
                        mailConfig = orderMail.getMailConfig();

                        SendMailLog sendMailLog = new SendMailLog();
                        sendMailLog.setUserId(userId);
                        sendMailLog.setSendType(templateId);
                        sendMailLog.setOrderCode(buy.getOrderCode());

                        sendMailLogService.sendMail(mailConfig, sendMailLog, buyer.getEmail(),buyer.getUserName(), config);
                    }
                }

                if (ObjectUtils.isEmpty(buyer.getMobile()) == false) {

                    Ums ums = umsService.getUms(templateId);
                    ApplicationInfo applicationInfo = applicationInfoService.getApplicationInfo(userId);

                    unifiedMessagingService.sendMessage(new OrderNew(ums, buy, userId, config, buyer.getMobile(), applicationInfo, cryptor, dataMasking));

                }

                // 발송하고 로그 기록
                orderMapper.insertOrderSendMessageLog(orderSendMessageLog);

            } else if ("order_deposit_wait".equals(templateId)) {

                long userId = buy.getUserId();

                Buyer buyer = buy.getBuyer();
                MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
                if (mailConfig != null) {

                    if (ObjectUtils.isEmpty(buyer.getEmail()) == false) {

                        OrderMail orderMail = new OrderMail(buy, userId, mailConfig, config, cryptor, dataMasking);
                        mailConfig = orderMail.getMailConfig();

                        SendMailLog sendMailLog = new SendMailLog();
                        sendMailLog.setUserId(userId);
                        sendMailLog.setSendType(templateId);
                        sendMailLog.setOrderCode(buy.getOrderCode());
                        sendMailLogService.sendMail(mailConfig, sendMailLog, buyer.getEmail(),buyer.getUserName(), config);
                    }
                }

                if (ObjectUtils.isEmpty(buyer.getMobile()) == false) {

                    for(BuyPayment payment : buy.getPayments()) {

                        if ("bank".equals(payment.getApprovalType())
                            || "vbank".equals(payment.getApprovalType())) {

                            Ums ums = umsService.getUms(templateId);
                            ApplicationInfo applicationInfo = applicationInfoService.getApplicationInfo(userId);
                            
                            if(ums != null && applicationInfo != null) {                            
                            	unifiedMessagingService.sendMessage(new OrderBank(ums, buy, payment, userId, config, buyer.getMobile(), applicationInfo, cryptor, dataMasking));
                            }
                        }
                    }
                }

                // 발송하고 로그 기록
                orderMapper.insertOrderSendMessageLog(orderSendMessageLog);
            }
        }
    }

    @Override
    public void sendOrderMessageTx(Order order, String templateId, Config config) {

        if (order == null) {
            return;
        }

        order.encrypt(orderEncryptor);

        OrderSendMessageLog orderSendMessageLog = new OrderSendMessageLog();
        orderSendMessageLog.setOrderCode(order.getOrderCode());
        orderSendMessageLog.setTemplateId(templateId);

        if ("order_delivering".equals(templateId)) {
            orderSendMessageLog.setDeliveryNumber(order.getMessageTargetDeliveryNumber());
        }

        // 사용자 발송 체크
        if (orderMapper.getOrderSendMessageLogCount(orderSendMessageLog) == 0) {

            long userId = order.getUserId();
            MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
            if (mailConfig != null) {

                if (ObjectUtils.isEmpty(order.getEmail()) == false) {
                    //배송중 메일 설정
                    if ("order_delivering".equals(templateId)) {
                        OrderMail orderMail = new OrderMail(order, userId, mailConfig, config, cryptor, dataMasking);
                        mailConfig = orderMail.getMailConfig();
                    } else if ("order_cready_payment".equals(templateId)) {
                        // 주문완료(입금대기->결제완료 바뀔때 or 주문시 바로 결제완료) 메일 설정
                        OrderMail orderMail = new OrderMail(order, userId, mailConfig, config, cryptor, dataMasking);
                        mailConfig = orderMail.getMailConfig();
                    }

                    SendMailLog sendMailLog = new SendMailLog();
                    sendMailLog.setUserId(userId);
                    sendMailLog.setSendType(templateId);
                    sendMailLog.setOrderCode(order.getOrderCode());
                    sendMailLogService.sendMail(mailConfig, sendMailLog, order.getEmail(), order.getUserName(), config);
                }
            }

            if (ObjectUtils.isEmpty(order.getMobile()) == false) {

                Ums ums = umsService.getUms(templateId);
                ApplicationInfo applicationInfo = applicationInfoService.getApplicationInfo(userId);

                if ("order_delivering".equals(templateId)) {
                    unifiedMessagingService.sendMessage(new OrderDelivering(order, userId, config, ums, order.getMobile(), applicationInfo, cryptor, dataMasking));
                } else if ("order_cready_payment".equals(templateId)) {
                    unifiedMessagingService.sendMessage(new OrderNew(ums, order, userId, config, order.getMobile(), applicationInfo, cryptor, dataMasking));
                }
            }

            // 발송하고 로그 기록
            orderMapper.insertOrderSendMessageLog(orderSendMessageLog);
        }
    }
}
