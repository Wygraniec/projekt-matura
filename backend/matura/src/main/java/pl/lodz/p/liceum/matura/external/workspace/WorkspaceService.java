package pl.lodz.p.liceum.matura.external.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import pl.lodz.p.liceum.matura.domain.workspace.FileWasNotFoundException;
import pl.lodz.p.liceum.matura.domain.workspace.RepositoryAlreadyResidesInDestinationFolderException;
import pl.lodz.p.liceum.matura.domain.workspace.RepositoryWasNotFoundException;
import pl.lodz.p.liceum.matura.domain.workspace.Workspace;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class WorkspaceService implements Workspace {

    private final String baseWorkspace;

    @Override
    public String createWorkspace(String sourceRepositoryUrl) {
        String absoluteDestinationPath = crateDirectoryPath();
        cloneRepository(sourceRepositoryUrl, absoluteDestinationPath);
        return absoluteDestinationPath;
    }

    @Override
    public Map<String, Object> readTaskDefinitionFile(final String rootPathUrl) {

        String fullPath = java.nio.file.Paths.get(rootPathUrl, "task_definition.yml").toString();

        Map<String, Object> data;
        try {
            // Create ObjectMapper and YAMLFactory
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            // Read YAML file
            data = mapper.readValue(new File(fullPath), Map.class);
        } catch (IOException e) {
            throw new FileWasNotFoundException();
        }

        return data;
    }

    @Override
    public void writeFile(String rootPathUrl, String path, byte[] bytes) {
        String fullPath = java.nio.file.Paths.get(rootPathUrl, path).toString();

        try {
            Files.write(Paths.get(fullPath), bytes);
        } catch (IOException e) {
            throw new FileWasNotFoundException();
        }
    }

    @Override
    public byte[] readFile(String rootPathUrl, String path) {
        String fullPath = java.nio.file.Paths.get(rootPathUrl, path).toString();

        try {
            return Files.readAllBytes(Paths.get(fullPath));
        } catch (IOException e) {
            throw new FileWasNotFoundException();
        }
    }

    @Override
    public void commitChanges(String rootPathUrl) {
        CommitCommand commit;
        try (Git git = Git.open(new File(rootPathUrl))) {

            AddCommand add = git.add();
            add.addFilepattern(".").call();

            commit = git.commit();
            commit.setMessage("update commit").call();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            throw new RepositoryWasNotFoundException();
        }
    }

    private void unlinkRemotes(Git git) {
        Set<String> remoteNames = git.getRepository().getRemoteNames();
        remoteNames.forEach(g -> {
            try {
                git.remoteRemove().setRemoteName(g).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });
    }

    private String crateDirectoryPath() {
        Path generatedDirectoryPath = java.nio.file.Paths.get(baseWorkspace, UUID.randomUUID().toString());
        return generatedDirectoryPath.toAbsolutePath().toString();
    }

    private void cloneRepository(String sourceRepositoryUrl, String destinationPath) {
        File repo = new File(destinationPath);
        Git git;
        try {
            git = Git.cloneRepository()
                    .setURI(sourceRepositoryUrl)
                    .setDirectory(repo)
                    .call();
        } catch (JGitInternalException | GitAPIException e) {
            throw new RepositoryAlreadyResidesInDestinationFolderException();
        }
        unlinkRemotes(git);
        git.close();
    }
}
