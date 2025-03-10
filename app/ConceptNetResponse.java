import com.google.gson.annotations.SerializedName;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConceptNetResponse {

    @SerializedName("results")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public static class Result {
        @SerializedName("node")
        private Node node;

        public Node getNode() {
            return node;
        }

        public static class Node {
            @SerializedName("label")
            private String label;

            public String getLabel() {
                return label;
            }
        }
    }
}


public class RetrofitClient {

    private static final String BASE_URL = "http://api.conceptnet.io/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}