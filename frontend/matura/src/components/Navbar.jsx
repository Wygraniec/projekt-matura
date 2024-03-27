import {useNavigate} from "react-router-dom";
import {
    Avatar,
    Box,
    Button,
    Flex,
    Heading,
    HStack,
    Menu,
    MenuButton,
    MenuItem,
    MenuList
} from "@chakra-ui/react";
import {logout, User} from "../services/userService.js";


export const Navbar = () => {
    const navigate = useNavigate();
    const user = User.fromLocalStorage();

    let colorMode = localStorage.getItem('chakra-ui-color-mode')
    const changeColorMode = () => {
        localStorage.setItem('chakra-ui-color-mode', colorMode === 'light'? 'dark' : 'light');
        colorMode = colorMode === 'light'? 'dark' : 'light';
        window.location.reload();
    }

    return (
        <Flex
            as="nav"
            align="center"
            justify="space-between"
            padding="1rem"
            bg='#7A3530'
        >
            <Box>
                <Button size='lg' variant="link" color="white" onClick={() => navigate('/dashboard')} margin='10px'>
                    Strona główna
                </Button>
                <Button size='lg' variant="link" color="white" onClick={() => navigate('/tasks')} margin='10px'>
                    Zbiór zadań
                </Button>
            </Box>

            <Menu>
                <MenuButton as={Button} colorMode='dark'>
                    <HStack>
                        <Heading size='md' color='white'>{user.username}</Heading>
                        <Avatar size='sm' name={user.username}/>
                    </HStack>
                </MenuButton>

                <MenuList>
                    <MenuItem><i className="fa-solid fa-user fa-fw"/> Profil</MenuItem>
                    <MenuItem onClick={changeColorMode}>
                        <i className={'fa-solid fa-fw ' + (colorMode === 'light'? 'fa-moon' : 'fa-sun')}/>
                        Ustaw {colorMode === 'light'? 'ciemny' : 'jasny'} motyw
                    </MenuItem>
                    <MenuItem><i className='fa-solid fa-gear fa-fw'/> Ustawienia </MenuItem>
                    <MenuItem onClick={logout}><i className="fa-solid fa-right-from-bracket fa-fw"/> Wyloguj się</MenuItem>
                </MenuList>
            </Menu>
        </Flex>
    )
}