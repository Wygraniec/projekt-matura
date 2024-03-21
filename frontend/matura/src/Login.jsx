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
    Button, AlertIcon, Alert
} from "@chakra-ui/react";
import {Formik, useField} from "formik";
import PropTypes from "prop-types";
import * as Yup from "yup";

const InputField = ({label, ...props}) => {
    const [field, meta] = useField(props);

    return (
        <FormControl id={name}>
            <FormLabel htmlFor={props.name}>{label}</FormLabel>
            <Input {...field} {...props}/>
            {
                meta.touched && meta.error ? (
                    <Alert className='error' status='error' mt='2'>
                        <AlertIcon/>
                        {meta.error}
                    </Alert>
                ) : null
            }
        </FormControl>
    )
}
InputField.propTypes = {
    label: PropTypes.object,
    name: PropTypes.string.isRequired,
    type: PropTypes.string,
    placeholder: PropTypes.string,
    id: PropTypes.string
}

const Login = () => (
    <Formik
        initialValues={{email: '', password: ''}}

        onSubmit={(values) => console.log(values)}

        validationSchema={
            Yup.object({
                email: Yup.string()
                    .email("Podana wartość musi być poprawnym adresem email")
                    .required("Pole nie może być puste"),

                password: Yup.string()
                    .required("Pole nie może być puste")
            })
        }
        validateOnMount={true}>

        {({isValid, isSubmitting}) => (
            <Flex justifyContent="center" alignItems="center" height="100vh">
                <Card minHeight={'35dvh'} minWidth={"30dvw"}>
                    <CardHeader>
                        <Heading>Logowanie</Heading>
                    </CardHeader>
                    <CardBody>
                        <Stack>

                            <InputField name={'email'} label={<><i className="fa-solid fa-envelope"/> Adres email:</>}
                                        type='email'/>

                            <InputField name={'password'} label={<><i className="fa-solid fa-lock"/> Hasło:</>}
                                        type='password'/>

                        </Stack>

                        <Button
                            type='submit'
                            marginY={'25px'}
                            colorScheme='blue'
                            disabled={!isValid || isSubmitting}>
                            Zaloguj się
                        </Button>

                    </CardBody>
                </Card>
            </Flex>
        )}
    </Formik>
)

export default Login
