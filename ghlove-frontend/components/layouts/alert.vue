<template>
    <div id="op-alert" class="modal fade p_notitle" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="pop_txt">
                        <p></p>
                    </div>
                    <div class="row no-gutters btn-group alert-type">
                        <div class="">
                            <button type="button" class="btn btn_lg btn_primary op-modal-ok" data-dismiss="modal" @click="clickEvent">확인</button>
                        </div>
                    </div>
                    <div class="row no-gutters btn-group confirm-type">
                        <div class="">
                            <button type="button" class="btn btn_lg btn_default op-modal-cancel" data-dismiss="modal" @click="cancelEvent">취소</button>
                        </div>
                        <div class="">
                            <button type="button" class="btn btn_lg btn_primary op-modal-ok" data-dismiss="modal" @click="clickEvent">확인</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<style scoped>
.modal .modal-body{
    padding:0;
}
.btn-group .btn{
    min-width:120px;
    padding: 16px 32px;
    
}
.confirm-type {
    justify-content: space-between;
}
</style>

<script>
    module.exports = {
        methods: {
            clickEvent: function (e) {
                e.stopPropagation();
                $s.callbackAlert(false);
            },
            cancelEvent: function (e) {
            	e.stopPropagation();
            	$s.cancelCallback(true);
            }
        },
    };
</script>