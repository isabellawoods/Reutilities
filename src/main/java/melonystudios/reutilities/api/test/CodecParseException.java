package melonystudios.reutilities.api.test;

/// Thrown when a codec fails to parse or its contents do not match the intended output.
/// @since *Reutilities* `1.6.0`
public class CodecParseException extends RuntimeException {
    /// Thrown when a codec fails to parse or its contents do not match the intended output.
    /// @param message A message detailing what caused this exception.
    public CodecParseException(String message) {
        super(message);
    }
}
