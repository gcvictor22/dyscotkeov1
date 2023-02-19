import axios from "axios"
import { useNavigate } from "react-router-dom";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

const userName = window.location.href.split("/")[4]
const url = `/user/userName/${userName}`

export const User = () => {

    const navigate = useNavigate();

    axios.get(url, { headers: headers }).then((res) => {
        console.log(res.data);
    }).catch((er) => {
        console.log(er);
        if (er.response.data.message === "The user: undefined does not exist" && window.location.href.split("/")[4] !== "undefined") {
            window.location.reload();
        }else if(er.response.status === 404){
            navigate("*");
        }
    });

    return (
        <div>
        </div>
    )

}

export default User;