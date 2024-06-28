import {Box, Divider, Text} from "@chakra-ui/react";
import ReactMarkdown from "react-markdown";
import PropTypes from "prop-types";

export const RenderMarkdown = ({document}) =>
    <>
        <Box
            borderRadius="md"
            maxWidth="95%"
            width="full"
            marginX="25px"
            borderColor='gray'
            borderWidth="1px"
            borderStyle="solid"
            paddingX="2.5em"
            paddingY='1.5em'
        >
            <ReactMarkdown components={{
                paragraph: (props) => <Text as='p' {...props}/>,

                h1: (props) => <Text as='h1' fontSize='3xl' fontWeight='bold' {...props} />,
                h2: (props) => <Text as='h2' fontSize='2xl' fontWeight='bold' {...props} />,
                h3: (props) => <Text as='h3' fontSize='xl' fontWeight='bold' {...props} />,
                h4: (props) => <Text as='h4' fontSize='lg' fontWeight='bold' {...props} />,
                h5: (props) => <Text as='h5' fontSize='md' fontWeight='bold' {...props} />,
                h6: (props) => <Text as='h6' fontSize='sm' fontWeight='bold' {...props} />,

                list: (props) => <Box as='ul' {...props} />,
                listItem: (props) => <Box as='li' {...props} />,
                orderedList: (props) => <Box as='ol' {...props} />,

                code: (props) => <Text as='code' {...props} />,
                inlineCode: (props) => <Text as='code' fontSize='sm' {...props} />,
                blockquote: (props) => <Box as='blockquote' borderLeft='4px solid gray.300'
                                            p={4} {...props} />,

                strong: (props) => <Text as='strong' fontWeight='bold' {...props} />,
                em: (props) => <Text as='em' fontStyle='italic' {...props} />,

                hr: (props) => <Divider {...props}/>,

                link: (props) => (
                    <Text as='a' color='blue.500'
                          _hover={{textDecoration: 'underline'}} {...props} />
                )
            }}>
                {document}
            </ReactMarkdown>
        </Box>
    </>

RenderMarkdown.propTypes = {
    document: PropTypes.string
}