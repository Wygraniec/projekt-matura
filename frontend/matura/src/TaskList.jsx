import {withAuthentication} from "./routeAuthentication.jsx";
import {Link, Navigate, useLocation} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {getTemplates, Template, TemplatePage} from "./services/templateService.js";
import {useEffect, useMemo, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardBody,
    CardHeader, Divider,
    Flex,
    Image,
    Spinner,
    Text
} from "@chakra-ui/react";
import PropTypes from "prop-types";
import {motion} from 'framer-motion'
import ReactMarkdown from "react-markdown";

export const PaginationLinks = ({totalPages, currentPage}) => {
    const paginationLinks = useMemo(() => {
        const links = [];

        for (let i = 1; i <= totalPages; i++) {
            links.push(
                <Link key={i} to={`/tasks?page=${i - 1}`}>
                    <Button size="md" variant={currentPage === i - 1 ? "solid" : "outline"} margin='5px' width='2px'>
                        {i}
                    </Button>
                </Link>
            );
        }

        return links;
    }, [totalPages, currentPage]);

    return <>{paginationLinks}</>;
};
PaginationLinks.propTypes = {
    totalPages: PropTypes.number,
    currentPage: PropTypes.number
}

const LanguageIcon = ({language, ...props}) => {
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


const TemplateCard = ({template, ...props}) => {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <Card {...props} marginY='4px'>
            <CardHeader>
                <Flex justifyContent='space-between' alignItems='center'>
                    <Flex alignItems='center'>
                        <LanguageIcon language={template.language} boxSize='50px'/>
                        <Text marginX='5px'>{template.source}</Text>
                    </Flex>

                    <Flex flexDirection='column'>
                        {/* TODO handle opening tasks reasonably (user whom it was assigned should be displayed the task with an appropriate id, whereas the others should have a new task created by them - for themselves)*/}
                        <Link to={``}>
                            <Button marginY='5px'>
                                <i className="fa-solid fa-code fa-fw"/>
                                <Text marginLeft='5px'>Rozwiąż</Text>
                            </Button>
                        </Link>
                        <Button onClick={() => setIsExpanded(!isExpanded)} marginY='5px'>
                            <i className={`fa-solid fa-arrow-${isExpanded ? "up" : "down"} fa-fw`}/>
                            <Text marginLeft='5px'>Polecenie</Text>
                        </Button>
                    </Flex>
                </Flex>
            </CardHeader>
            {isExpanded && (
                <motion.div
                    initial={{height: '0'}}
                    animate={{height: '100%'}}
                    transition={{
                        duration: '0.5',
                    }}
                >
                    <CardBody display='flex' justifyContent='center'>
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
                                {template.statement}
                            </ReactMarkdown>
                        </Box>
                    </CardBody>
                </motion.div>
            )}
        </Card>
    )
}
TemplateCard.propTypes = {
    template: Template
}


const TaskList = () => {
    const location = useLocation();
    const [templatePage, setTemplatePage] = useState(new TemplatePage());
    const [loading, setLoading] = useState(true);

    let page = Number(new URLSearchParams(location.search).get("page"));
    page = page === null ? 0 : page;

    useEffect(() => {
        getTemplates(page, 4).then(
            page => {
                setTemplatePage(page);
                setLoading(false);
            }
        );
    }, [page]);

    // TODO create an option to filter the available templates
    return (
        <Subpage>
            {page >= templatePage.totalPages ? <Navigate to="/tasks"/> : null}

            {loading && (
                <Card display="flex" alignItems="center" justifyContent="center">
                    <CardBody textAlign="center">
                        <Spinner size="xl"/>

                        <Text>Ładowanie zadań</Text>
                    </CardBody>
                </Card>
            )}

            {!loading && (
                <>
                    {templatePage.templates.map((template) => (
                        <TemplateCard template={template} id={template.id} key={template.id}/>
                    ))}

                    <Flex justifyContent='center' flexWrap='wrap'>
                        <PaginationLinks totalPages={templatePage.totalPages} currentPage={page}/>
                    </Flex>
                </>
            )}
        </Subpage>
    );
};

export const TaskListWithAuth = withAuthentication(TaskList)
