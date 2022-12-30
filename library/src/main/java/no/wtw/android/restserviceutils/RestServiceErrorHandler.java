package no.wtw.android.restserviceutils;

public class RestServiceErrorHandler {

    public static final String ERROR_MESSAGE_HEADER = "x-wtw-errormessage";
    private static final String TAG = RestServiceErrorHandler.class.getSimpleName();

/*
    private RestServiceErrorObject getErrorObject(ClientHttpResponse response) {
        StringWriter writer = new StringWriter();
        try {
            InputStream stream = response.getBody();
            IOUtils.copy(stream, writer, "UTF-8");
            if (stream != null)
                stream.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read error body");
            Log.e(TAG, e.getMessage());
        }
        try {
            return new Gson().fromJson(writer.toString(), RestServiceErrorObject.class);
        } catch (JsonSyntaxException e) {
            return new RestServiceErrorObject(writer.toString());
        }
    }
*/

}