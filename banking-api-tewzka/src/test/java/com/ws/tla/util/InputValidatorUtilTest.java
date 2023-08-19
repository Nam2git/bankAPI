package com.ws.tla.util;

import com.ws.tla.exception.NoDataException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class InputValidatorUtilTest {

    @InjectMocks
    private InputValidatorUtil inputValidatorUtil;

    @Before
    public void Setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NoDataException.class)
    public void testNullCheck() throws NoDataException {

        inputValidatorUtil.nullCheck(null);
    }
}
