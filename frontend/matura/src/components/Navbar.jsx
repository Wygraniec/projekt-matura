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
    MenuDivider,
    MenuGroup,
    MenuItem,
    MenuList,
    Text,
    useColorMode
} from "@chakra-ui/react";
import {logout, User} from "../services/userService.js";


export const Navbar = () => {
    const {colorMode, toggleColorMode} = useColorMode();
    const navigate = useNavigate();
    const user = User.fromLocalStorage();

    if(!user)
        navigate('/')

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
                    <i className="fa-solid fa-house"/><Text marginX='5px'>Strona główna</Text>
                </Button>

                <Button size='lg' variant="link" color="white" onClick={() => navigate('/tasks')} margin='10px'>
                    <i className="fa-solid fa-fw fa-book-open"/> <Text marginX='5px'>Zbiór zadań</Text>
                </Button>

                <Menu>
                    <MenuButton as={Button} size='lg' variant="link" color='white' marginX='10px'>
                        Moje zadania <i className="fa-solid fa-fw fa-angle-down"/>
                    </MenuButton>
                    <MenuList>
                        {user.role === "INSTRUCTOR" && (
                            <>
                                <MenuGroup title='Przypisywanie'>
                                    <MenuItem>Zarządzaj</MenuItem>
                                    <MenuItem>Przypisz uczniowi</MenuItem>
                                    <MenuItem>Przypisz grupie</MenuItem>
                                </MenuGroup>
                                <MenuDivider/>
                                <MenuGroup title='Zadania'>
                                    <MenuItem>Zarządzaj</MenuItem>
                                    <MenuItem>Stwórz</MenuItem>
                                </MenuGroup>
                            </>
                        )}

                        {user.role === "STUDENT" && (
                            <>
                                <MenuItem onClick={() => navigate('/activeTasks')}>
                                    <i className="fa-solid fa-fw fa-clipboard"/><Text marginX='2px'>Aktywne</Text>
                                </MenuItem>

                                <MenuItem onClick={() => navigate('/finishedTasks')}>
                                    <i className="fa-solid fa-fw fa-check-double"/><Text marginX='2px'>Rozwiązane</Text>
                                </MenuItem>
                            </>
                        )}

                    </MenuList>
                </Menu>
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

                    <MenuItem onClick={logout}><i className="fa-solid fa-right-from-bracket fa-fw"/>
                        Wyloguj się
                    </MenuItem>
                </MenuList>
            </Menu>
        </Flex>
    )
}