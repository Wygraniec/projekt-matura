import {
    Box,
    Flex,
    Heading,
    Text,
    Button,
    Link,
} from '@chakra-ui/react';

export const ErrorElement = () => (
    <Box
        bg="gray.900"
        minH="100vh"
        display="flex"
        justifyContent="center"
        alignItems="center"
        py={20}
    >
        <Flex
            direction="column"
            align="center"
            maxW="lg"
            px={4}
            py={12}
            bg="gray.800"
            borderRadius="lg"
            boxShadow="xl"
        >
            <i className="fas fa-exclamation-triangle" style={{ fontSize: 48, color: '#7A3530' }} />
            <Heading as="h1" size="xl" color="white" mt={4}>
                Ups, coś poszło nie tak!
            </Heading>
            <Text color="gray.400" mt={4}>
                Przepraszamy za niedogodności. Wygląda na to, że wystąpił błąd po naszej stronie.
            </Text>
            <Text color="gray.400" mt={4}>
                Chcesz pomóc nam się poprawić? {''}
                <Link href="mailto:support@example.com" color="#7A3530">
                    Zgłoś błąd
                </Link>
            </Text>
            <Button
                mt={8}
                background={'#7A3530'}
                variant="solid"
                size="lg"
                onClick={() => window.location.href = '/'}
            >
                Wróć na stronę główną
            </Button>
        </Flex>
    </Box>
);