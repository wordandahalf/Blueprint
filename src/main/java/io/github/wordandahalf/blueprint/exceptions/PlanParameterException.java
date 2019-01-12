package io.github.wordandahalf.blueprint.exceptions;

import javassist.CtClass;

public class PlanParameterException extends Exception {
    private String method;
    private CtClass[] expectedParameters;

    public PlanParameterException(String method, CtClass[] expectedParameters) {
        this.method = method;
        this.expectedParameters = expectedParameters;
    }

    @Override
    public String getMessage() {
        String expectedParametersString = "";

        for(int i = 0; i < expectedParameters.length; i++) {
            expectedParametersString += expectedParameters[i].getName() + ", ";
        }

        //To remove the trailing ', '
        expectedParametersString = expectedParametersString.substring(0, expectedParametersString.length() - 2);

        return "Expected parameters '" + expectedParametersString + "' at method '" + method + "'";
    }
}
