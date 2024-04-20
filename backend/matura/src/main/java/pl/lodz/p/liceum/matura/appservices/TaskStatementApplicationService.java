package pl.lodz.p.liceum.matura.appservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.domain.template.Template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Log
public class TaskStatementApplicationService {

    public String readTaskStatement(Template template) {
        try {
            String branch = getDefaultBranch(template);
            URL url = new URL(getStatementUrl(template, branch));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            return responseBuilder.toString();
        } catch (IOException e) {
            throw new TaskStatementCannotBeReadException();
        }
    }
    private String getDefaultBranch(Template template) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String APIUrl = getGitHubAPIUrl(template);
        JsonNode node = mapper.readTree(new URL(APIUrl));
        return node.get("default_branch").asText();
    }

    private String getGitHubAPIUrl(Template template) {
        return template.getSourceUrl().replace("github.com", "api.github.com/repos");
    }
    private String getStatementUrl(Template template, String branchName) {
        return template.getSourceUrl().replace("github.com", "raw.githubusercontent.com") + "/" + branchName + "/readme.md";
    }
}
