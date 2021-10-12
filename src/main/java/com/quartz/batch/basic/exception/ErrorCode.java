package com.quartz.batch.basic.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /*
        400 : Bad Request, 요청이 부적절 할 때, 유효성 검증 실패, 필수 값 누락 등.
        401 : Unauthorized, 인증 실패, 로그인하지 않은 사용자 또는 권한 없는 사용자 처리
        402 : Payment Required
        403 : Forbidden, 인증 성공 그러나 자원에 대한 권한 없음. 삭제, 수정시 권한 없음.
        404 : org.aspectj.weaver.ast.Not Found, 요청한 URI에 대한 리소스 없을 때 사용.
        405 : Method Not Allowed, 사용 불가능한 Method를 이용한 경우.
        406 : Not Acceptable, 요청된 리소스의 미디어 타입을 제공하지 못할 때 사용.
        408 : Request Timeout
        409 : Conflict, 리소스 상태에 위반되는 행위 시 사용.
        413 : Payload Too Large
        423 : Locked
        428 : Precondition Required
        429 : Too Many Requests
        500 : 서버 에러
    */
    SUCCESS(200,  "success" )
    , NOT_EXIST_JOB(400, "요청하신 JOB 은 존재하지 않습니다.")
    , EXIST_JOB_NAME(400, "요청하신 JOB 명칭은 중복됩니다.")
    , BAD_REQUEST(401,"BAD_REQUEST")
    , SYSTEM_ERROR(500 , "SYSTEM_ERROR")
    ;

    private int status;
    private String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
