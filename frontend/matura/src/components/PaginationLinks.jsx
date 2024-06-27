import {useMemo} from "react";
import {Link, useLocation} from "react-router-dom";
import {Button, HStack} from "@chakra-ui/react";
import PropTypes from "prop-types";

export const PaginationLinks = ({totalPages, currentPage}) => {

    const currentPageUrl = useLocation().pathname;

    const paginationLinks = useMemo(() => {
        const links = [];

        for (let i = 1; i <= totalPages; i++) {
            links.push(
                <Link key={i} to={`${currentPageUrl}?page=${i - 1}`}>
                    <Button size="md" variant={currentPage === i - 1 ? "solid" : "outline"} margin='5px' width='2px'>
                        {i}
                    </Button>
                </Link>
            );
        }

        return links;
    }, [totalPages, currentPageUrl, currentPage]);

    return <HStack maxWidth='70dvw'>
        {currentPage - 1 >= 0 ? (
            <Link to={`${currentPageUrl}?page=${currentPage - 1}`}>
                <Button size="md" margin="5px" width="2px">
                    <i className="fa-solid fa-angle-left"/>
                </Button>
            </Link>
        ) : (
            <Button size="md" margin="5px" width="2px" isDisabled>
                <i className="fa-solid fa-angle-left"/>
            </Button>
        )}

        <HStack spacing={0} overflowX='scroll' css={{
            '&::-webkit-scrollbar': {
                width: '10px',
                height: '10px',
            },
            '&::-webkit-scrollbar-track': {
                backgroundColor: 'rgba(0, 0, 0, 0.1)',
            },
            '&::-webkit-scrollbar-thumb': {
                backgroundColor: 'rgba(0, 0, 0, 0.3)',
                borderRadius: '5px',
            },
            '&::-webkit-scrollbar-thumb:hover': {
                backgroundColor: 'rgba(0, 0, 0, 0.5)',
            },
        }}>
            {paginationLinks.map((paginationLink) => (
                paginationLink
            ))}
        </HStack>

        {currentPage + 1 < totalPages ? (
            <Link to={`${currentPageUrl}?page=${currentPage + 1}`}>
                <Button size="md" margin="5px" width="2px">
                    <i className="fa-solid fa-angle-right"/>
                </Button>
            </Link>
        ) : (
            <Button size="md" margin="5px" width="2px" isDisabled>
                <i className="fa-solid fa-angle-right"/>
            </Button>
        )}
    </HStack>;
};
PaginationLinks.propTypes = {
    totalPages: PropTypes.number,
    currentPage: PropTypes.number
}