import {useNavigate} from "react-router-dom";
import {
    Avatar,
    Box,
    Button,
    DarkMode,
    Flex,
    Heading,
    HStack,
    Menu,
    MenuButton,
    MenuItem,
    MenuList,
    useColorMode
} from "@chakra-ui/react";
import {logout, User} from "../services/userService.js";


export const Navbar = () => {
    const { colorMode, toggleColorMode } = useColorMode();
    const navigate = useNavigate();
    const user = User.fromLocalStorage();

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
                <Button size='lg' variant="link" color="white" onClick={() => navigate('/mytasks')} margin='10px'>
                    Moje zadania
                </Button>
            </Box>

            <Menu>
                <DarkMode>
                    <MenuButton as={Button}>
                        <HStack>
                            <Heading size='md' color='white'>{user.username}</Heading>
                            <Avatar size='sm' name={user.username}/>
                        </HStack>
                    </MenuButton>
                </DarkMode>


                <MenuList>
                    <MenuItem><i className="fa-solid fa-user fa-fw"/> Profil</MenuItem>
                    <MenuItem onClick={toggleColorMode}>
                        <i className={'fa-solid fa-fw ' + (colorMode === 'light' ? 'fa-moon' : 'fa-sun')}/>
                        Ustaw {colorMode === 'light' ? 'ciemny' : 'jasny'} motyw
                    </MenuItem>
                    <MenuItem><i className='fa-solid fa-gear fa-fw'/> Ustawienia </MenuItem>
                    <MenuItem onClick={logout}><i className="fa-solid fa-right-from-bracket fa-fw"/> Wyloguj
                        się</MenuItem>
                </MenuList>
            </Menu>
        </Flex>
    )
}