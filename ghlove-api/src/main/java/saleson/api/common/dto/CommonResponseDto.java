package saleson.api.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class CommonResponseDto<D> {

	private final String result;
	private final String message;
	private final D data;

	public static <D> CommonResponseDto<D> ofSuccess() {
	    return new CommonResponseDto<>(ResponseCode.SUCCESS.name(), ResponseCode.SUCCESS.getMessage(), null);
	}

	public static <D> CommonResponseDto<D> ofSuccess(D data) {
	    return new CommonResponseDto<>(ResponseCode.SUCCESS.name(), ResponseCode.SUCCESS.getMessage(), data);
	}

	public static <D> CommonResponseDto<D> ofFail() {
	    return new CommonResponseDto<>(ResponseCode.FAIL.name(), ResponseCode.FAIL.getMessage(), null);
	}

	public static <D> CommonResponseDto<D> ofFail(String result, String message) {
	    return new CommonResponseDto<>(result, message, null);
	}

}
