import {Navigate} from "react-router-dom";
import {logout, User} from './services/userService.js'

const permissionLevels = {
    'STUDENT': 1,
    'INSTRUCTOR': 2,
    'ADMIN': 3
};

const isAuthenticated = () => {
    try {
        return User.fromLocalStorage().validate()
    } catch (e) {
        // There's no user saved locally, therefore no one is logged in
        logout()
    }
}

export const withAuthentication = (Component, requiredPermission = 'STUDENT') => {
    return function WithAuthenticationWrapper(props) {
        return (!isAuthenticated() || permissionLevels[User.fromLocalStorage().role] < permissionLevels[requiredPermission])?
            <Navigate to='/login'/> : <Component {...props} />
    };
};
