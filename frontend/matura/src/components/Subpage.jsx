import {Navbar} from "./Navbar.jsx";
import {Box} from "@chakra-ui/react";

// eslint-disable-next-line react/prop-types
export const Subpage = ({children}) => (
    <>
        <Navbar/>

        <Box paddingX='20px' paddingY='10px' margin='10px' minHeight='90dvh'>
            {children}
        </Box>
    </>
)
