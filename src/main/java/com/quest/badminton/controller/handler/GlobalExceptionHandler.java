package com.quest.badminton.controller.handler;

import com.quest.badminton.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.quest.badminton.constant.ErrorConstants.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity <ErrorResponse> handleAccessDenied(BadRequestException ex) {
        String errorMessage = getErrorMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorCode(ex.getMessage())
                        .errorMessage(errorMessage)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity <ErrorResponse> handleAccessDenied(Exception ex) {
        log.error(ex.getMessage(), ex);
        String errorMessage = getErrorMessage(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .errorCode(ERR_INTERNAL_SERVER)
                        .errorMessage(errorMessage)
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }


    private String getErrorMessage(String errorCode) {
        return switch (errorCode) {
            case ERR_EMAIL_EXISTED -> "Email đã tồn tại";
            case ERR_USER_INACTIVE -> "Tài khoản chưa kích hoạt";
            case ERR_USER_NOT_FOUND -> "Không tìm thấy tài khoản";
            case ERR_CREDENTIALS_INVALID -> "Thông tin xác thực không chính xác";
            case ERR_USER_REGISTERED_REFEREE -> "Đã đăng ký làm trọng tài";
            case ERR_PLAYER_REGISTRATION_PENDING_APPROVE -> "Đơn đăng ký đang được xét duyệt";
            case ERR_PLAYER_REGISTRATION_APPROVED -> "Đã tham gia vào giải đấu";
            case ERR_PLAYER_BANNED -> "Đã bị cấm trong giải đấu";
            case ERR_TOUR_NOT_FOUND -> "Không tìm thấy giải đấu";
            case ERR_GROUP_MATCH_NOT_FOUND -> "Không tìm thấy vòng đấu";
            case ERR_GROUP_MATCH_NOT_IN_TOUR -> "Vòng đấu không có trong giải đấu";
            case ERR_PLAYER_NOT_FOUND -> "Không tìm thấy vận động viên";
            case ERR_MALE_PLAYER_FULL -> "Đã hết số lượng thành viên nam trong giải đấu";
            case ERR_FEMALE_PLAYER_FULL -> "Đã hết số lượng thành viên nữ trong giải đấu";
            case ERR_TEAM_NAME_EXISTS -> "Tên Team đã tồn tại";
            case ERR_TEAM_NOT_FOUND -> "Không tìm thấy Team";
            case ERR_PLAYER_NOT_IN_TOUR -> "Vận động viên không có trong giải đấu";
            case ERR_PLAYER_NOT_IN_TEAM -> "Vận động viên không có trong Team";
            case ERR_PLAYER_PAIR_EXISTS -> "Cặp đấu đã tồn tại";
            case ERR_PLAYER_PAIR_NOT_FOUND -> "Không tìm thấy cặp đấu";
            case ERR_PLAYER_PAIR_NOT_IN_TOUR -> "Cặp đấu không có trong giải đấu";
            default -> "Có lỗi xảy ra";
        };
    }
}
