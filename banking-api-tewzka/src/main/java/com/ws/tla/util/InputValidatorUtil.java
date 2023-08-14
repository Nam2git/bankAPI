package com.ws.tla.util;

import com.ws.tla.exception.NoDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class InputValidatorUtil {

    public void checkNotNull(Object obj) throws NoDataException {
        if (Objects.isNull(obj)) {
            log.error("No data found");
            throw new NoDataException("No new account info to create");
        }
    }
}
