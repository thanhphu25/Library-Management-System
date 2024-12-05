package LMS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class API {

    /**
     * Phương thức để lấy phản hồi HTTP từ URL được cung cấp
     * @param url URL để lấy phản hồi
     * @return phản hồi HTTP dưới dạng chuỗi
     */
    public String getHttpResponse(String url) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            System.out.println("Getting " + url);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + status);
            }
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Phương thức để lấy danh sách sách từ JSON được cung cấp
     * @param json JSON để lấy sách
     * @return danh sách sách từ JSON
     */
    public ArrayList<Book> getBooksFromJson(String json) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            JsonNode node = new ObjectMapper().readTree(json);
            if (node.has("items")) {
                for (JsonNode item : node.get("items")) {
                    JsonNode volumeInfo = item.get("volumeInfo");
                    Book book = new Book();

                    // Các trường khác
                    book.setTitle(volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown");
                    book.setAuthor(volumeInfo.has("authors") ? volumeInfo.get("authors").get(0).asText() : "Unknown");
                    book.setIsbn(volumeInfo.has("industryIdentifiers") && volumeInfo.get("industryIdentifiers").size() > 0
                            ? volumeInfo.get("industryIdentifiers").get(0).get("identifier").asText() : "Unknown");
                    book.setDescription(volumeInfo.has("description") ? volumeInfo.get("description").asText() : "No description available");

                    // Trích xuất giá trị query từ JSON
                    String query = volumeInfo.has("query") ? volumeInfo.get("query").asText() : "Unknown";

                    // Trích xuất previewLink từ JSON
                    String previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").asText() : "No preview link available";

                    // Chuyển đổi previewLink thành định dạng mong muốn
                    String updatedPreviewLink = convertToUpdatedPreviewLink(previewLink, query);
                    book.setPreviewLink(updatedPreviewLink);

                    // Xử lý hình ảnh
                    JsonNode imageLinksNode = volumeInfo.get("imageLinks");
                    book.setImageLink(imageLinksNode != null && imageLinksNode.has("smallThumbnail")
                            ? imageLinksNode.get("smallThumbnail").asText() : "No image link available");

                    books.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    /**
     * Phương thức để chuyển đổi previewLink thành định dạng mong muốn
     * @param previewLink previewLink để chuyển đổi
     * @param query query để thêm vào previewLink
     * @return previewLink đã được chuyển đổi
     */
    private String convertToUpdatedPreviewLink(String previewLink, String query) {
        if (previewLink.contains("books.google.com.vn/books?id=")) {
            // Lấy book ID từ previewLink (từ id= đến &)
            String bookId = previewLink.substring(previewLink.indexOf("id=") + 3, previewLink.indexOf("&"));

            return "https://books.google.com.vn/books?id=" + bookId + "&dq=" + query + "&hl=vi&source=gbs_navlinks_s";
        }
        return previewLink;
    }
}
