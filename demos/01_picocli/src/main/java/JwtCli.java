import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import picocli.CommandLine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import picocli.CommandLine.*;

@Command(
    name = "jwt-cli",
    version = "Version 0.0.1",
    sortOptions = false,
    usageHelpWidth = 60,
    header =
            "   _____                  _         \n" +
            "  /  __ \\                | |  _    \n" +
            "  | /  \\/ __ _ _ __   __ | |_| |_  \n" +
            "  | |    / _` | '_ \\ / _`| |_   _| \n" +
            "  | \\__/\\ (_| | | | | (_|| | |_|  \n" +
            "  \\____/\\__,_|_| |_|\\__,_|_|     \n",

    footer = "Provided by Canal+",
    description = "Custom @|bold,underline styles|@ and @|fg(red) colors|@."
)
public class JwtCli implements Callable<Void> {

  @Option(names = {"-d", "--decode"}, arity = "0..1",description = "decode mode")
  private boolean decode;

  @Option(names = {"-i", "--issuer"}, order = 1, description = "jwt issuer")
  private String issuer;

  @Option(names = {"-t", "--token"}, paramLabel = "", order = 2, description = "token to decode")
  private String token;

  @Option(names = {"-c", "--claim"}, arity = "1..*",description = "add a claim(s)")
  private Map<String, String> claims;

  @Option(names = {"-e", "--expiresAt"},description = "expiration time (YYYY-MM-DD)")
  private LocalDate expiresAt;

  @Option(names = {"-o", "--output"}, defaultValue = "table", description = "output format: table,text,json")
  private OutputFormat outputFormat;

  @Option(names = {"-s", "--secret"}, description = "Passphrase", defaultValue = "secret")
  private String secret;

  @Option(names = {"-v", "--verbose"}, description = {
      "Specify multiple -v options to increase verbosity.",
      "For example, `-v -v -v` or `-vvv`"})

  private boolean[] verbosity = new boolean[0];

  @Option(names = {"-V", "--version"}, versionHelp = true, description = "display version info")
  boolean versionInfoRequested;

  @CommandLine.Spec
  CommandLine.Model.CommandSpec spec;

  public static void main(String[] args) {
    CommandLine.call(new JwtCli(), args);
  }


  public enum OutputFormat {
    json,
    text,
    table

  }

  @Override
  public Void call() throws Exception {

    if (decode) {
      checkDecodeUsage();
      Algorithm algorithm = Algorithm.HMAC256(secret);
      JWTVerifier verifier = JWT.require(algorithm).build();

      String tokenAsStr = token;
      if ("-".equals(token)) {
        tokenAsStr = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);
      }

      DecodedJWT jwt = verifier.verify(tokenAsStr);

      String result = null;
      switch (outputFormat) {
        case json: {
          result = formatJson(toMap(jwt));
          break;
        }
        case text: {
          result = formatText(toMap(jwt));
          break;
        }
        case table: {
          result = formatTable(toMap(jwt));
          break;
        }
      }

      System.out.println(result);

    } else {
      checkEncodeUsage();
      LocalDate expire = expiresAt == null ? LocalDate.now()
          .withMonth(6) : expiresAt;
      Algorithm algorithm = Algorithm.HMAC256(secret);

      JWTCreator.Builder builder = JWT.create()
          .withIssuer(this.issuer)
          .withExpiresAt(Date.from(expire.atStartOfDay(ZoneId.systemDefault()).toInstant()));

      claims.forEach(builder::withClaim);
      String token = builder.sign(algorithm);

      System.out.println(token);

    }

    return null;
  }


  private void checkDecodeUsage() {
    if (expiresAt != null) {
      throw new CommandLine.ParameterException(spec.commandLine(),
          "Invalid option expiresAt '--expiresAt=<expiresAt>'");
    }

    if (token == null || token.isBlank()) {
      throw new CommandLine.ParameterException(spec.commandLine(),
          "Missing required option '--token=<token>'");
    }
  }

  private void checkEncodeUsage() {
    if (claims == null || claims.isEmpty()) {
      throw new CommandLine.ParameterException(spec.commandLine(),
          "Missing required option '--claim =<claim>'");
    }

  }


  private Map<String, String> toMap(DecodedJWT jwt) {
    return jwt.getClaims().entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey,
                e -> {
                  if (e.getValue().asString() == null) {
                    return DateTimeFormatter.ISO_INSTANT.format(
                        e.getValue().asDate().toInstant()
                    );
                  } else {
                    return e.getValue().asString();
                  }
                }
            )
        );
  }

  private String formatJson(Map<String, String> allClaims) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(allClaims);
  }

  private String formatText(Map<String, String> allClaims) {
    final StringBuilder buf = new StringBuilder();
    allClaims.forEach(
        (String name, String value) -> buf.append(name).append(":").append(value).append("\n")
    );
    return buf.toString();
  }

  private String formatTable(Map<String, String> allClaims) {
    Optional<Integer> nameColumnMax = allClaims
        .keySet()
        .stream()
        .map(String::length)
        .max(Integer::compareTo);

    Optional<Integer> valueMax = allClaims
        .values()
        .stream()
        .map(String::length)
        .max(Integer::compareTo);


    CommandLine.Help.TextTable textTable =
        CommandLine.Help.TextTable.forColumnWidths(
            CommandLine.Help.Ansi.AUTO, nameColumnMax.orElse(30) + 20, valueMax.orElse(30));

    textTable.addRowValues("Name", "Value");
    textTable.addEmptyRow();
    allClaims.forEach(
        (String name, String value) -> textTable.addRowValues(name, value)
    );

    return textTable.toString();
  }

}




