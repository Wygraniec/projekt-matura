import {Navigate} from "react-router-dom";
import {User} from './services/userService.js'

const permissionLevels = {
    'STUDENT': 1,
    'INSTRUCTOR': 2,
    'ADMIN': 3
};

const isAuthenticated = () => {
    return User.fromLocalStorage().validate()
}

export const withAuthentication = (Component, requiredPermission = 'STUDENT') => {
    return function WithAuthenticationWrapper(props) {
        return (!isAuthenticated() || permissionLevels[User.fromLocalStorage().role] < permissionLevels[requiredPermission])?
            <Navigate to='/login'/> : <Component {...props} />
    };
};
