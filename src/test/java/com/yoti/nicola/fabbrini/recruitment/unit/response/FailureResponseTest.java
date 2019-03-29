package com.yoti.nicola.fabbrini.recruitment.unit.response;

import com.yoti.nicola.fabbrini.recruitment.enumerator.Error;
import com.yoti.nicola.fabbrini.recruitment.response.FailureResponse;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class FailureResponseTest {

    @Test(expected = NullPointerException.class)
    public void testWithNullError() {
        new FailureResponse(null);
    }

    @Test
    public void testFormat() {
        final FailureResponse response = new FailureResponse(Error.INVALID_INSTRUCTION);
        final JSONObject jsonObject = new JSONObject(response.toString());

        Assert.assertEquals("INVALID_INSTRUCTION", jsonObject.getString("error_codename"));
        Assert.assertEquals("The concatenated instruction string can only contain the following characters: N,S,W,E.", jsonObject.getString("error_message"));
    }
}
