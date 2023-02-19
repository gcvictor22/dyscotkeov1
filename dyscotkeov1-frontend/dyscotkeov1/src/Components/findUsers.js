import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

const url = "/user/?page=0"
var it = 0;

export const FindUsers = () => {

    const navigate = useNavigate();
    const [responseData, setResponseData] = useState([]);

    if (it < 1) {
        axios.get(url, { headers: headers }).then((res) => {
            setResponseData(res.data.content);
            it = 1;
        }).catch((er) => {
            console.log(er);
            if (er.response.status === 401 || er.response.status === 500 || er.response.status === 403) {
                Swal.fire({
                    icon: 'info',
                    title: 'No puedes acceder a esta sección sin iniciar sesión',
                    showDenyButton: true,
                    denyButtonColor: '#4c007d',
                    confirmButtonText: 'Iniciar sesión',
                    confirmButtonColor: '#1465bb',
                    denyButtonText: 'Registrarme',
                }).then((result) => {
                    if (result.isDenied) {
                        navigate("/register")
                    } else {
                        navigate("/")
                    }
                })
            }
        })
    }


    return (
        <div>
            {
                responseData.map((u) => {
                    return <button key={u.id} onClick={() => navigate(`/user/${u.userName}`)}>{u.userName}</button>
                })
            }
        </div>
    )

}

export default FindUsers;