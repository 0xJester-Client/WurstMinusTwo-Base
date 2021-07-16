package me.third.right.utils.Interpreters;

/**
 * Not my own code.
 * @Author GeeksforGeeks
 * https://www.geeksforgeeks.org/brainfuck-interpreter-java/
 */
public class BrainFuckInterpreter {
    private StringBuilder outputBuffer = new StringBuilder();
    private final int length = 28;
    private int index = 0;
    private int c = 0;
    private int ptr = 0;
    private String code;
    private String inputTemp = null;
    private boolean requireInput = false;
    private boolean providedInput = false;
    private byte[] memory = new byte[length];
    private String state = "Doing nothing...";

    public void interpret() {
        while (index < code.length()) {
            if (code.charAt(index) == '>') {
                if (ptr == length - 1) ptr = 0;
                else ptr++;
            }

            else if (code.charAt(index) == '<') {
                if (ptr == 0) ptr = length - 1;
                else ptr--;
            }

            else if (code.charAt(index) == '+') memory[ptr] ++;
            else if (code.charAt(index) == '-') memory[ptr] --;
            else if (code.charAt(index) == '.') outputBuffer.append((char)(memory[ptr]));
            else if (code.charAt(index) == ',') {
                if(providedInput) {
                    memory[ptr] = (byte) inputTemp.charAt(0);
                    providedInput = false;
                    requireInput = false;
                    inputTemp = null;
                } else if(!requireInput) {
                    state = "Waiting for input...";
                    requireInput = true;
                    break;
                }
            }

            else if (code.charAt(index) == '[') {
                if (memory[ptr] == 0) {
                    index++;
                    while (c > 0 || code.charAt(index) != ']') {
                        if (code.charAt(index) == '[')
                            c++;
                        else if (code.charAt(index) == ']')
                            c--;
                        index++;
                    }
                }
            }

            else if (code.charAt(index) == ']') {
                if (memory[ptr] != 0) {
                    index--;
                    while (c > 0 || code.charAt(index) != '[') {
                        if (code.charAt(index) == ']') c ++;
                        else if (code.charAt(index) == '[') c --;
                        index--;
                    }
                    index--;
                }
            }
            index++;
            if(index >= code.length()) state = "Finished.";
        }
    }

    public String getOutputBuffer() {
        return outputBuffer.toString();
    }

    public void setInputTemp(String inputTemp) {
        this.inputTemp = inputTemp;
        this.providedInput = true;
        this.requireInput = false;
    }

    public void reset() {
        index = 0;
        ptr = 0;
        c = 0;
        outputBuffer = new StringBuilder();
        memory = new byte[length];
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getState() {
        return state;
    }
}
