import {withAuthentication} from "./routeAuthentication.jsx";
import {useLocation} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {CodeEditor} from "./components/CodeEditor.jsx";

const TaskList = () => {
    const location = useLocation();

    let page = new URLSearchParams(location.search).get('page');
    page = page === null ? 1 : page;

    return (
        <Subpage>

        </Subpage>
    )
}

export const TaskListWithAuth = withAuthentication(TaskList)
