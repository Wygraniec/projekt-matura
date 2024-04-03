import {withAuthentication} from "./routeAuthentication.jsx";
import {Navbar} from "./components/Navbar.jsx";

const Dashboard = () => {
    return (
        <>
            <Navbar/>
        </>
    )
}

export const DashboardWithAuth = withAuthentication(Dashboard)
