import {logout, User} from "./services/userService.js";
import {withAuthentication} from "./routeAuthentication.jsx";

const Dashboard = () => {
    const user = User.fromLocalStorage();

    return (
        <>
            <h2>Zalogowano</h2>
            <p>{user.id}</p>
            <p>{user.username}</p>
            <p>{user.email}</p>
            <p>{user.role}</p>

            <button onClick={logout}>Wyloguj</button>
        </>
    )
}

export const DashboardWithAuth = withAuthentication(Dashboard)
