import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class PL0Lexer {
    // 定义词法规则

    /**
     * 全局变量
     **/

    //关键字的正则表达式
    //"\b"在正则表达式中代表单词边界，是为了之后将单词完全匹配
    private static final String KEYWORDS = "\\b(CONST|VAR|PROCEDURE|BEGIN|END|ODD|IF|THEN|CALL|WHILE|DO|READ|WRITE)\\b";
    //标识符的正则表达式
    //要求必须由字母开头，可以包含字符和数字，*号代表闭包
    private static final String IDENTIFIER = "[a-zA-Z][a-zA-Z0-9]*";
    //数字的正则表达式
    //“\d”在正则表达式中代表匹配数字的元字符，+代表正则闭包
    private static final String NUMBER = "\\d+";
    //操作符的正则表达式
    //存在其中负号需要双反斜杠转义
    private static final String OPERATORS = "([:<>]=)|[+\\-*/=<>]";
    //定界符（分隔符）的正则表达式
    private static final String DELIMITERS = "[,.;()\\[\\]]";
    //正则表达式模式
    private static final String TOKEN_PATTERN = String.format("(%s)|(%s)|(%s)|(%s)|(%s)", KEYWORDS, IDENTIFIER, NUMBER, OPERATORS, DELIMITERS);
    //类别
    private static final String[] TOKEN_KINDS={"KEYWORDS", "IDENTIFIER","NUMBER", "OPERATORS", "DELIMITERS", "Unknown"};

    //SYM：存放每个单词的类别，为内部编码的表示形式
    public static List<String> sym=new ArrayList<>();
    //ID：存放用户所定义的标识符的值，即标识符字符串的机内表示
    public static List<String> id=new ArrayList<>();
    //NUM：存放用户定义的数,范围为带符号的32位整数
    public static List<Integer> num=new ArrayList<>();

    // 进行词法分析的方法
    public static void getSYM(String sourceCode) {
        //将正则表达式编译为pattern对象
        Pattern pattern = Pattern.compile(TOKEN_PATTERN);
        //matcher类在输入的字符串上处理匹配
        Matcher matcher = pattern.matcher(sourceCode);

        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (matcher.group(i) != null) {
                    //getToken
                    String token = matcher.group(i);
                    //token类型
                    String tokenType;

                    //通过捕获组的组号来判断类别
                    switch (i){
                        case 1:
                        case 2:
                            tokenType = TOKEN_KINDS[0];
                            break;
                        case 3:
                            //判断token的标识符串长度是否超出10个字符
                            if(token.length()>10){
                                //输出错误
                                tokenType="Error:IdentifierOVERFLOW";
                            }
                            else{
                                tokenType = TOKEN_KINDS[1];
                                //将用户自定义的标识符存入id数组
                                id.add(token);
                            }
                            break;
                        case 4:
                            tokenType = TOKEN_KINDS[2];

                            try {
                                // 尝试将字符串转换为整数
                                int value = Integer.parseInt(token);

                                //将数字存入num数组
                                num.add(value);

                            } catch (NumberFormatException e) {
                                // 如果转换失败，说明超过了整数的表示范围
                                tokenType = "Error:NumberOVERFLOW";
                            }
                            break;
                        case 5:
                        case 6:
                            tokenType = TOKEN_KINDS[3];
                            break;
                        case 7:
                            tokenType = TOKEN_KINDS[4];
                            break;
                        default:
                            tokenType = TOKEN_KINDS[5];

                    }

                    //通过二次正则验证来判断组号
//                    if (token.matches(KEYWORDS)) {
//                        tokenType = "Keyword";
//                    } else if (token.matches(IDENTIFIER)) {
//                        //判断token的标识符串长度是否超出10个字符
//                        if(token.length()>10){
//                            //输出错误
//                            tokenType="Error:IdentifierOverflow";
//                        }
//                        else{
//                            tokenType = "Identifier";
//                            //将用户自定义的标识符存入id数组
//                            id.add(token);
//                        }
//                    } else if (token.matches(NUMBER)) {
//                        tokenType = "Number";
//
//                        try {
//                            // 尝试将字符串转换为整数
//                            int value = Integer.parseInt(token);
//
//                            //将数字存入num数组
//                            num.add(value);
//
//                        } catch (NumberFormatException e) {
//                            // 如果转换失败，说明超过了整数的表示范围
//                        }
//                    } else if (token.matches(OPERATORS)) {
//                        tokenType = "Operator";
//                    } else if (token.matches(DELIMITERS)) {
//                        tokenType = "Delimiter";
//                    } else {
//                        tokenType = "Unknown";
//                    }
                    sym.add(tokenType);

                    System.out.println("Token: " + token + ",\t\t\t Type: " + tokenType);
                    break;
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        String sourceCode = "VAR x; \rPROCEDURE foo; \rBEGIN x >= 10; \rEND;";
        String sourceCode = "VAR abcdefghijklmnopqrstuvwxyz; \rBEGIN x >= 100000000000000000000;";

        // 调用词法分析方法
        getSYM(sourceCode);
    }
}

