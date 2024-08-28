import {Card, CardBody, Spinner, Text} from "@chakra-ui/react";

export function LoadingCard() {
    return <Card display="flex" alignItems="center" justifyContent="center">
        <CardBody textAlign="center">
            <Spinner size="xl"/>

            <Text>Wczytywanie</Text>
        </CardBody>
    </Card>;
}