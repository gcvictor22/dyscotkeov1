import axios, { } from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

export const Profile = () => {

    const { userName } = useParams();
    var url = `/user/userName/${userName}`

    //var loggedUser = localStorage.getItem('loggedUser');

    const [user, setUser] = useState(null);
    const [img, setImg] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [file, setFile] = useState();
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(url, { headers: headers }).then((res) => {
            setUser(res.data);
        }).catch((er) => {
            console.log(er);
            if (er.response.status === 401 && localStorage.getItem('loggedUser') === userName) {
                window.location.reload();

            } else if (er.response.status === 404) {
                navigate("*");
            } else {
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
        setIsLoading(false);
        // eslint-disable-next-line
    }, []);

    useEffect(() => {
        if (isLoading) {
            setImg('http://localhost:8080/file/default.png')
        } else if (user != null) {
            setImg(`http://localhost:8080/file/${user.imgPath}`);
        }
        // eslint-disable-next-line
    }, [])

    const imgSubmit = (e) => {
        e.preventDefault();
        headers["Content-Type"] = "multipart/form-data"
        let formData = new FormData();
        formData.append("file", file);

        axios.post("/file/upload", formData, { headers: headers, }).then((res) => {
            setImg(res.data.uri);
            console.log(img);
            if (res.status === 201) {
                Swal.fire({
                    icon: 'success',
                    title: '¡Foto actualizada con éxito!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.reload();
                })
            }
        }).catch((er) => {
            Swal.fire({
                icon: 'error',
                title: er.response['data']['message'],
                showConfirmButton: false,
                timer: 1500
            })
        })
    }

    if (isLoading) {
        return (
            <div>
                <p>Cargando</p>
            </div>
        )
    } else if (user !== null) {
        return (
            <div>
                <div className="container">

                    <div className="userNameProfile">
                        <img src={`http://localhost:8080/file/${user.imgPath}`} className="imgUser" alt="" />
                        <h1>{user.userName}</h1>
                    </div>
                    <p>{user.fullName}</p>

                    <ul className="nav nav-tabs enlacesPaginacion">
                        <li className="active"><a href="#Home" data-toggle="tab">Home</a></li>
                        <li><a href="#Product" data-toggle="tab">Product</a></li>
                        <li><a href="#CSS" data-toggle="tab">CSS</a></li>
                        <li><a href="#Javascript" data-toggle="tab">Javascript</a></li>
                        <li><a href="#Bootstrap" data-toggle="tab">Bootstrap</a></li>
                        <li><a href="#Jquery" data-toggle="tab">Jqeury</a></li>
                        <li><a href="#Contact" data-toggle="tab">Contact</a></li>
                    </ul>


                    <div className="tab-content">
                        <div id="Home" className="tab-pane fade in active">
                            <h3>Home</h3>
                            <p>Blind would equal while oh mr do style. Lain led and fact none. One preferred sportsmen resolving the happiness continued. High at of in loud rich true. Oh conveying do immediate acuteness in he. Equally welcome her set nothing has gravity whether parties. Fertile suppose shyness mr up pointed in staying on respect. </p>
                        </div>
                        <div id="Product" className="tab-pane fade">
                            <h3>Product</h3>
                            <p>Advanced extended doubtful he he blessing together. Introduced far law gay considered frequently entreaties difficulty. Eat him four are rich nor calm. By an packages rejoiced exercise. To ought on am marry rooms doubt music. Mention entered an through company as. Up arrived no painful between. It declared is prospect an insisted pleasure. </p>
                        </div>
                        <div id="Contact" className="tab-pane fade">
                            <h3>Modificar foto de perfil</h3>
                            <form onSubmit={imgSubmit}>
                                <div className="wrapper">
                                <div className="file-upload">
                                    <input type="file" onChange={(e) => setFile(e.target.files[0])}/>
                                    <i className="fa fa-upload" aria-hidden="true"></i>
                                </div>
                                </div>
                                <input type="submit" value="Guardar" id="submitSaveImg"></input>
                            </form>
                        </div>
                        <div id="CSS" className="tab-pane fade">
                            <h3>CSS Tutorial</h3>
                            <p>CSS is a stylesheet language that describes the presentation of an HTML (or XML) document. CSS describes how elements must be rendered on screen, on paper, or in other media. This tutorial will teach you CSS from basic to advanced.</p>
                        </div>
                        <div id="Javascript" className="tab-pane fade">
                            <h3>Javascript Tutorial</h3>
                            <p>JavaScript is the programming language of HTML and the Web. Programming makes computers do what you want them to do. JavaScript is easy to learn. This tutorial will teach you JavaScript from basic to advanced.</p>
                        </div>
                        <div id="Bootstrap" className="tab-pane fade">
                            <h3>Bootstrap Tutorial</h3>
                            <p>Bootstrap is the most popular HTML, CSS, and JS framework for developing responsive, mobile first projects on the web.</p>
                        </div>
                        <div id="Jquery" className="tab-pane fade">
                            <h3>Jquery Tutorial</h3>
                            <p>jQuery UI is a curated set of user interface interactions, effects, widgets, and themes built on top of the jQuery JavaScript Library. Whether you're building highly interactive web applications or you just need to add a date picker to a form control, jQuery UI is the perfect choice.</p>
                        </div>
                    </div>

                </div>
            </div>
        );
    }
}

export default Profile;