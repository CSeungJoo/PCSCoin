package kr.pah.pcs.pcscoin.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> {
    private T data;
    private boolean isError;

    public Result(T data) {
        this.data = data;
        this.isError = false;
    }
}
