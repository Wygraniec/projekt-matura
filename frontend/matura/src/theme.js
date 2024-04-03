import { extendTheme } from '@chakra-ui/react'

const theme = extendTheme({
    config: {
        initialColorMode: 'system',
        // useSystemColorMode: 'true'
    },
})

export default theme