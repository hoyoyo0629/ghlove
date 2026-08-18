package saleson.common.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Path {
    private String path;
    private String container;
    private String prefix;
    private String filename;

    public Path(String uploadRoot, String uploadPath) {

        if (StringUtils.hasText(uploadRoot) && StringUtils.hasText(uploadPath)) {

            uploadPath = uploadPath.replace("\\", "/");
            if (uploadPath.indexOf(".") > -1) {
                uploadPath = uploadPath.replace(uploadRoot, "");
                if (!uploadPath.startsWith("/")) uploadPath = "/"+uploadPath;

                String[] pathInfo = StringUtils.delimitedListToStringArray(uploadPath, "/");
                String filename = pathInfo[pathInfo.length - 1];
                String pathContainer = uploadPath.replace("/" + filename, "");

                setFilename(filename);
                setPath(pathContainer);

            } else {

                if (!uploadPath.startsWith("/")) uploadPath = "/"+uploadPath;

                setFilename("");
                setPath(uploadPath);
            }

            setContainer(StringUtils.delimitedListToStringArray(getPath(), "/")[0]);
            setPrefix(getPath().replace(getContainer() + "/", ""));
        }
    }
}
