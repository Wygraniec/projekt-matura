import {
    Card,
    CardHeader,
    CardBody,
    Flex,
    FormControl,
    FormLabel,
    Input,
    Stack,
    Heading,
    Button
} from "@chakra-ui/react";

// import {Form, Formik} from "formik";
// import * as Yup from "yup";

// {
//     <Formik
//         initialValues={{email: '', password: ''}}
//         onSubmit={(values) => alert(JSON.stringify(values))}
//
//         validationSchema={
//             Yup.object({
//                 email: Yup.string()
//                     .email("Nie podano poprawnego adresu email")
//                     .required("Należy podać adres email"),
//
//                 password: Yup.string()
//                     .required("Należy podać hasło")
//             })
//         }
//     >
//
//         {(isValid, isSubmitting) => {
//             <Form>
//
//             </Form>
//         }}
//
//     </Formik>
// }

const Login = () => (
    <Flex justifyContent="center" alignItems="center" height="100vh">
        <Card minHeight={'35dvh'} minWidth={"30dvw"}>
            <CardHeader>
                <Heading>Logowanie</Heading>
            </CardHeader>
            <CardBody>
                <Stack>
                    <FormControl id='email'>
                        <FormLabel><i className="fa-solid fa-envelope" /> Adres email:</FormLabel>
                        <Input type='email'/>
                    </FormControl>

                    <FormControl id='password'>
                        <FormLabel><i className="fa-solid fa-lock"/> Hasło:</FormLabel>
                        <Input type='password'/>
                    </FormControl>
                </Stack>

                <Button marginY={'25px'} colorScheme={'blue'}>Zaloguj się</Button>

            </CardBody>
        </Card>
    </Flex>
)


export default Login
