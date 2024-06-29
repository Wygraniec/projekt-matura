import {Image} from "@chakra-ui/react";
import PropTypes from "prop-types";

export const LanguageIcon = ({language, ...props}) => {
    switch (language) {
        case "CPP":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/cplusplus/cplusplus-original.svg"
                {...props}
            />
        case "PYTHON":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/python/python-original.svg"
                {...props}
            />
        case "C_SHARP":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/csharp/csharp-original.svg"
                {...props}
            />
        case "JAVA":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg"
                {...props}
            />
    }
}
LanguageIcon.propTypes = {
    language: PropTypes.string.isRequired
}