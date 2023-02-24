import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import { faCheckCircle, faArrowLeft, faHeart, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json',
};

export const Profile = () => {

    const { userName } = useParams();
    var url = `/user/userName/${userName}`

    //var loggedUser = localStorage.getItem('loggedUser');

    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [file, setFile] = useState();
    const [files, setFiles] = useState([]);
    const [fileName, setFileName] = useState('');
    const [postsPage, setPostsPage] = useState([]);
    const [postLoading, setPostLoading] = useState(true);
    const [pageNumber, setPageNumber] = useState(0);
    const [affair, setAffair] = useState('');
    const [content, setContent] = useState('');
    const [numResCar, setNumResCar] = useState(250);
    const [editUserName, setEditUserName] = useState('');
    const [editPhoneNumber, setEditPhoneNumber] = useState('');
    const [editEmail, setEditEmail] = useState('');
    const [editFullName, setEditFullName] = useState('');
    const [pageContent, setPageContent] = useState([]);
    const [publishedPosts, setPublishedPosts] = useState([]);
    const [allUsers, setAllUsers] = useState([]);
    const [userPageNumber, setUserPageNumber] = useState(0);
    const [searchUserName, setSearchUserName] = useState('');
    const [loadingUsers, setLoadingUsers] = useState(true);
    const [likedPostsUser, setLikedPostsUser] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(url, { headers: headers }).then((res) => {
            setUser(res.data);
            setEditUserName(res.data.userName);
            setEditPhoneNumber(res.data.phoneNumber);
            setEditEmail(res.data.email);
            setEditFullName(res.data.fullName);
            setPublishedPosts(res.data.publishedPosts);
            setLikedPostsUser(res.data.likedPosts);
        }).catch((er) => {
            if (er.response.status === 404) {
                navigate("*");
            } else if (er.response.status === 500) {
                localStorage.clear();
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

    function getUser() {
        axios.get(url, { headers: headers }).then((res) => {
            setUser(res.data);
            setEditUserName(res.data.userName);
            setEditPhoneNumber(res.data.phoneNumber);
            setEditEmail(res.data.email);
            setEditFullName(res.data.fullName);
            setPublishedPosts(res.data.publishedPosts);
            setLikedPostsUser(res.data.likedPosts);
        }).catch((er) => {
            if (er.response.status === 404) {
                navigate("*");
            } else if (er.response.status === 500) {
                localStorage.clear();
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
    }

    const [loggedUser, setLoggedUser] = useState(null);
    useEffect(() => {
        axios.get(`/user/profile`, { headers: headers }).then((res) => {
            setLoggedUser(res.data);
        })
    }, [])
    function comprobarUserLiked(post) {
        if (loggedUser !== null) {
            return loggedUser.likedPosts.filter((p) => p.id === post.id).length > 0;
        }
    }

    const createPost = (e) => {
        e.preventDefault();
        if (files.length > 4) {
            Swal.fire({
                icon: 'error',
                title: 'No se pueden subir más de 4 imágenes',
                showConfirmButton: false,
                timer: 1500
            })
        } else {
            axios.post("/post/", postBody, { headers: headers }).then((res) => {
                if (res.status === 201) {
                    if (files.length > 1) {
                        imgsPostSubmit(e, res.data.id);
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: '¡El post se ha publicado con éxito!',
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            window.location.reload();
                        })
                    }
                }
            }).catch((er) => {
                console.log(er);
                Swal.fire({
                    icon: 'error',
                    title: er.response.data.subErrors[0].message,
                    showConfirmButton: false,
                    timer: 1500
                })
            })
        }
    }

    const imgsPostSubmit = (e, postId) => {
        e.preventDefault();
        headers["Content-Type"] = "multipart/form-data"
        let formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i])
        }

        axios.post(`/file/upload/post/${postId}`, formData, { headers: headers }).then((res) => {
            if (res.status === 201) {
                Swal.fire({
                    icon: 'success',
                    title: '¡El post se ha publicado con éxito!',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    window.location.reload();
                })
            }
        }).catch((er) => {
            console.log(er, files);
            Swal.fire({
                icon: 'error',
                title: er.response['data']['message'],
                showConfirmButton: false,
                timer: 1500
            })
        })
    }

    const postBody = {
        affair: affair,
        content: content
    }

    useEffect(() => {
        axios.get(`/post/?page=${pageNumber}`, { headers: headers }).then((res) => {
            setPostsPage(res.data);
            setPageContent(res.data.content);
        }).catch((er) => {
        })
        setPostLoading(false);
        // eslint-disable-next-line
    }, [pageNumber]);

    useEffect(() => {
        axios.get(`/user/?page=${userPageNumber}&s=userName:${searchUserName}`, { headers: headers }).then((res) => {
            setLoadingUsers(false);
            setAllUsers(res.data);
        }).catch((er) => {
            console.log(er);
        })
    }, [userPageNumber, searchUserName])

    const follow = (u) => {
        var boton = document.getElementById("followBoton" + u.id);
        if (boton.style.color === "white") {
            boton.style.background = "white"
            boton.style.color = "#2590EB"

            boton.value = "  Seguir  "
        } else {
            boton.style.background = "#2590EB"
            boton.style.color = "white"
            boton.style.border = "2px solid white"
            boton.value = "Eliminar "
        }

        axios.post(`/user/follow/${u.userName}`, null, { headers: headers }).then((res) => {
            console.log(res);
        }).catch((er) => {
            console.log(er);
        })
    }

    function deletePost(post) {
        Swal.fire({
            title: '¿Seguro qué quieres eliminar el post?',
            showCancelButton: true,
            cancelButtonText: 'Cancelar',
            confirmButtonText: 'Eliminar',
            confirmButtonColor: 'red',
        }).then((result) => {
            if (result.isConfirmed) {
                var divPost = document.getElementById("post" + post.id);
                var divPost2 = document.getElementById("postTodos" + post.id);
                if (divPost !== null) {
                    divPost.style.display = "none"
                    divPost2.style.display = "none"
                }

                axios.delete(`/post/${post.id}`, { headers: headers }).catch(() => {
                    Swal.fire({
                        icon: "error",
                        title: "Ha ocurrido un error",
                        showConfirmButton: false,
                        timer: 1500
                    })
                })
            }
        })
    }

    function navigateTo(params) {
        if (params !== userName) {
            navigate(`/user/${params}`)
            window.location.reload()
        }
    }

    const imgSubmit = (e) => {
        e.preventDefault();
        headers["Content-Type"] = "multipart/form-data"
        let formData = new FormData();
        formData.append("file", file);

        axios.post("/file/upload", formData, { headers: headers, }).then((res) => {
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

    const likeAPost = (post) => {
        axios.post(`/post/like/${post.id}`, null, { headers: headers }).then((res) => {
            console.log(res);
        }).catch((er) => {
            console.log(er);
        })
    }

    function cambiarIcono(id) {
        var corazon = document.getElementById("corazon" + id);

        if (corazon.style.color === "red") {
            corazon.style.color = "white";
        } else {
            corazon.style.color = "red"
        }
    }

    const logOut = () => {
        localStorage.clear();
        navigate("/")
    }

    const editProfile = (e) => {
        e.preventDefault();
        axios.put(`/user/edit/profile`, editProfileBody, { headers: headers }).then((res) => {
            console.log(res);
            localStorage.setItem("loggedUser", editUserName);
        }).catch((er) => {
            Swal.fire({
                icon: "error",
                title: er.response.data.subErrors[0].message,
                timer: 1500,
                showConfirmButton: false
            })
        })
    }

    const editProfileBody = {
        username: editUserName,
        email: editEmail,
        phoneNumber: editPhoneNumber,
        fullName: editFullName
    }

    const [boolPostForm, setBoolPostForm] = useState(false);
    var postForm = document.getElementById("postForm");
    var backgroundPostForm = document.getElementById("backgroundFormPost")
    var prog = document.getElementById("porcentajaProg");

    function mostrarPostForm() {
        if (boolPostForm) {
            postForm.style.top = "-1000px"
            backgroundPostForm.style.visibility = "hidden"
            setBoolPostForm(false);
        } else {
            postForm.style.top = "25%"
            backgroundPostForm.style.visibility = "visible"
            setBoolPostForm(true);
        }
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
                <button id="backgroundFormPost" onClick={() => mostrarPostForm()}>
                </button>
                <div id="postForm">
                    <form onSubmit={createPost}>
                        <input type="text" maxLength="50" placeholder="Título?" onChange={(e) => setAffair(e.target.value)}></input>
                        <textarea onChange={(e) => { setContent(e.target.value); setNumResCar(250 - e.target.value.length); prog.style.width = (e.target.value.length * 70) / 250 + "px" }} maxLength="250" placeholder="¿Qué pasa esta noche?"></textarea>
                        <div className="porcentaje">
                            <p>{numResCar}</p><div id="porcentajeBG">
                                <div id="porcentajaProg"></div>
                            </div>
                        </div>
                        <div className="files-upload">
                            <input type="file" name="file" id="file" multiple className="inputfile" onChange={(e) => {
                                setFiles(e.target.files);
                            }} />
                            <label htmlFor="file"><i className="fa fa-cloud-upload"></i> Seleccione un archivo</label>
                        </div>
                        <input type="submit" value="Postear" id="submitSaveImg"></input>
                    </form>
                </div>
                <nav id="navBar">
                    <button id="logOutButton" onClick={() => logOut()}>Log out</button>
                    <button id="miPerfilButton" style={{color : "red", border: "2px solid red"}} onClick={() => {
                        Swal.fire({
                            title: '¿Seguro qué quieres eliminar tu cuenta? Esta acción eliminara todos tus posts, follows y followers',
                            showCancelButton: true,
                            cancelButtonText: 'Cancelar',
                            confirmButtonText: 'Eliminar',
                            confirmButtonColor: 'red',
                        }).then((result) => {
                            if (result.isConfirmed) {
                                navigate("/")
                                axios.delete(`/user/delete`, { headers: headers }).catch(() => {
                                    Swal.fire({
                                        icon: "error",
                                        title: "Ha ocurrido un error",
                                        showConfirmButton: false,
                                        timer: 1500
                                    })
                                })
                            }
                        })
                    }}>Eliminar cuenta</button>
                    <button id="miPerfilButton" onClick={() => navigate(`/user/${localStorage.getItem("loggedUser")}`)}>Mi perfil</button>
                    <button id="postearButton" onClick={() => mostrarPostForm()}>Postear</button>
                    <button id="goBack"><FontAwesomeIcon icon={faArrowLeft} size="2x" onClick={() => { window.history.back() }} /></button>
                </nav>
                <div id="fullBackground">
                    <div className="container">
                        <div className="userNameProfile">
                            <img src={`http://localhost:8080/file/${user.imgPath}`} className="imgUser" alt="" />
                            <h1>{user.userName} {user.verified === true &&
                                <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" />
                            }</h1>
                        </div>
                        <p className="userFullName">{user.fullName}</p>

                        <ul className="nav nav-tabs enlacesPaginacion">
                            {localStorage.getItem("loggedUser") === userName &&
                                <li className="active"><a href="#Posts" data-toggle="tab">Todos los posts</a></li>
                            }
                            {localStorage.getItem("loggedUser") === userName &&
                                <li><a href="#Usuarios" data-toggle="tab">Usuarios</a></li>
                            }
                            {localStorage.getItem("loggedUser") !== userName &&
                                <li className="active"><a href="#Publicaciones" data-toggle="tab">Publicaciones</a></li>
                            }
                            {localStorage.getItem("loggedUser") === userName &&
                                <li><a href="#Publicaciones" data-toggle="tab">Publicaciones</a></li>
                            }
                            <li><a href="#Javascript" onClick={() => getUser()} data-toggle="tab">Favoritos</a></li>
                            <li><a href="#Bootstrap" onClick={() => getUser()} data-toggle="tab">Followers</a></li>
                            <li><a href="#Jquery" onClick={() => getUser()} data-toggle="tab">Follows</a></li>
                            {localStorage.getItem('loggedUser') === userName &&
                                <li><a href="#Editar" data-toggle="tab">Editar mi perfil</a></li>
                            }
                        </ul>


                        <div className="tab-content">
                            {localStorage.getItem("loggedUser") === userName &&
                                <div id="Posts" className="tab-pane fade in active">
                                    <h3>Todos los posts</h3>
                                    <div id="divPosts">
                                        {postsPage.content === undefined &&
                                            <p id="noHayPost">No hay ninguna publicación</p>
                                        }
                                        {// eslint-disable-next-line
                                            postsPage !== undefined && postsPage.totalElements > 20 &&
                                            <div id="paginadorPosts">
                                                <div id="paginacion">
                                                    {postsPage.first !== true &&
                                                        <button id="atras" onClick={() => setPageNumber(pageNumber - 1)}>&#8678;</button>
                                                    }
                                                    {postsPage.totalElements > 20 &&
                                                        <button id="numPagina">{pageNumber + 1}</button>
                                                    }
                                                    {postsPage.last !== true &&
                                                        <button id="siguiente" onClick={() => setPageNumber(pageNumber + 1)}>&#8680;</button>
                                                    }
                                                </div>
                                            </div>
                                        }
                                        <br />
                                        {postLoading === false && postsPage.content !== undefined &&
                                            pageContent.map((p) => {
                                                var bool = p.likedByUser;
                                                return <div className="post" id={"postTodos" + p.id} key={p.id}>
                                                    <button className="userWhoPost" onClick={() => navigateTo(p.userWhoPost.userName)}>
                                                        <img src={`http://localhost:8080/file/${p.userWhoPost.imgPath}`} alt="" />
                                                        <p>{p.userWhoPost.userName}</p>
                                                        {p.userWhoPost.verified &&
                                                            <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" size="1x" className="verificadoUser" />
                                                        }
                                                    </button>
                                                    <span className="numberUsersWhoLiked" id={"numLike" + p.id}>{p.usersWhoLiked}</span>
                                                    <FontAwesomeIcon icon={faHeart} className="likePostButton" style={bool ? { color: "red" } : { color: "white" }} onClick={() => { likeAPost(p); cambiarIcono(p.id); var l = document.getElementById("numLike" + p.id); var c = document.getElementById("corazon" + p.id); c.style.color !== "white" ? l.innerHTML = parseInt(parseInt(l.innerHTML) + 1) : l.innerHTML = parseInt(parseInt(l.innerHTML) - 1); likedPostsUser.filter(po => po.id === p.id).length > 0 ? setLikedPostsUser(likedPostsUser) : setLikedPostsUser(likedPostsUser.push(p)) }} size={"2x"} id={"corazon" + p.id} color="red" />
                                                    <h3>{p.affair}</h3>
                                                    <p>{p.content}</p>
                                                    {p.imgPath.length > 0 && p.imgPath[0] !== "VACIO" &&
                                                        <div className="imgPostContainer">
                                                            {
                                                                p.imgPath.map((i) => {
                                                                    return <div className="sigleImgPost" key={i}><img src={`http://localhost:8080/file/${i}`} alt="" /></div>
                                                                })
                                                            }
                                                        </div>
                                                    }
                                                </div>
                                            })
                                        }
                                    </div>
                                </div>
                            }
                            {localStorage.getItem("loggedUser") === userName &&
                                <div id="Usuarios" className="tab-pane fade">
                                    <div id="buscador">
                                        <h3>Todos los usuario</h3>
                                        {// eslint-disable-next-line
                                            <input placeholder="Buscar..." type="search" onChange={(e) => (setSearchUserName(e.target.value), setUserPageNumber(0))}></input>
                                        }
                                    </div>
                                    <div id="divPosts">
                                        {allUsers === undefined &&
                                            <p id="noHayPost">No existen usuarios</p>
                                        }
                                        {// eslint-disable-next-line
                                            allUsers !== undefined && allUsers.totalElements > 20 &&
                                            <div id="paginadorPosts">
                                                <div id="paginacion">
                                                    {postsPage.first !== true &&
                                                        <button id="atras" onClick={() => setUserPageNumber(userPageNumber - 1)}>&#8678;</button>
                                                    }
                                                    {postsPage.totalElements > 20 &&
                                                        <button id="numPagina">{userPageNumber + 1}</button>
                                                    }
                                                    {postsPage.last !== true &&
                                                        <button id="siguiente" onClick={() => setUserPageNumber(userPageNumber + 1)}>&#8680;</button>
                                                    }
                                                </div>
                                            </div>
                                        }
                                        <br />
                                        {loadingUsers === false && allUsers !== undefined &&
                                            allUsers.content.map((u) => {
                                                var bool = u.followedByUser;
                                                var bool2 = u.followers >= 1;
                                                return <div key={u.id}>
                                                    {u.userName !== localStorage.getItem("loggedUser") &&
                                                        <div className="user" id={"userTodos" + u.id}>
                                                            <img src={`http://localhost:8080/file/${u.imgPath}`} alt="" />
                                                            <input type="button" id={"followBoton" + u.id} value={u.followedByUser ? "Eliminar " : "  Seguir  "} onClick={() => { follow(u); var f = document.getElementById("foll" + u.id); var v = document.getElementById("verified" + u.id); bool = !bool; bool ? f.innerHTML = parseInt((parseInt(f.textContent) + 1)) : f.innerHTML = parseInt((f.textContent - 1)); parseInt(document.getElementById("foll" + u.id).innerHTML) >= 1 ? bool2 = true : bool2 = !bool2; bool2 ? v.style.visibility = "visible" : v.style.visibility = "hidden" }} className="followUser" size="2x" style={u.followedByUser ? { border: "2px solid white", background: "#2590EB", color: "white" } : { color: "#2590EB", background: "white", border: "2px solid #2590EB" }} />
                                                            <div className="datasUsuarios">
                                                                <h2>{u.userName} <span><FontAwesomeIcon icon={faCheckCircle} color="white" size="xs" id={"verified" + u.id} style={bool2 ? { visibility: "visible" } : { visibility: "hidden" }} className="verificadoUser" /></span></h2>
                                                                <p>{u.fullName}</p>
                                                                <p><b>Followers: <span id={"foll" + u.id}>{u.followers > 999 ? u.followers > 1000000 ? (u.followers / 1000000).toFixed(1) + "M" : u.followers / 1000 : u.followers}</span>&nbsp;&nbsp;&nbsp;&nbsp;Posts: {u.countOfPosts}</b></p>
                                                            </div>
                                                        </div>
                                                    }
                                                </div>
                                            })
                                        }
                                    </div>
                                </div>
                            }
                            <div id="Editar" className="tab-pane fade">
                                <h3>Modificar foto de perfil</h3>
                                <form onSubmit={imgSubmit}>
                                    <div className="wrapper">
                                        <div className="file-upload">
                                            <input type="file" onChange={(e) => { setFile(e.target.files[0]); setFileName(e.target.files[0].name) }} />
                                            <i className="fa fa-upload" aria-hidden="true"></i>
                                        </div>
                                    </div>
                                    <p id="fileName">{fileName}</p>
                                    <input type="submit" value="Guardar" id="submitSaveImg"></input>
                                </form>
                                <hr />
                                <h3>Modificar datos personales</h3>
                                <form onSubmit={editProfile}>
                                    <div id="editForm">
                                        <input type="text" placeholder="userName" value={editUserName} onChange={(e) => setEditUserName(e.target.value)}></input>
                                        <input type="text" placeholder="fullName" value={editFullName} onChange={(e) => setEditFullName(e.target.value)}></input>
                                        <input type="text" placeholder="email" value={editEmail} onChange={(e) => setEditEmail(e.target.value)}></input>
                                        <input type="text" placeholder="phoneNumber" value={editPhoneNumber} onChange={(e) => setEditPhoneNumber(e.target.value)}></input>
                                    </div>
                                    <input type="submit" value="Guardar" id="submitSaveImg"></input>
                                </form>
                            </div>
                            <div id="Publicaciones" className="tab-pane fade in active">
                                <h3>Publicaciones de {userName}</h3>
                                <div id="divPosts">
                                    {publishedPosts.length < 1 &&
                                        <p id="noHayPost">No hay ninguna publicación</p>
                                    }
                                    <br />
                                    {postLoading === false && publishedPosts.length > 0 &&
                                        publishedPosts.map((p) => {
                                            return <div className="post" id={"post" + p.id} key={p.id}>
                                                <button className="userWhoPost" onClick={() => navigateTo(p.userWhoPost.userName)}>
                                                    <img src={`http://localhost:8080/file/${p.userWhoPost.imgPath}`} alt="" />
                                                    <p>{p.userWhoPost.userName}</p>
                                                    {p.userWhoPost.verified &&
                                                        <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" size="1x" className="verificadoUser" />
                                                    }
                                                </button>
                                                {p.userWhoPost.userName === localStorage.getItem("loggedUser") &&
                                                    < FontAwesomeIcon icon={faTrash} className="deletePostButton" onClick={() => deletePost(p)} size="2x" />
                                                }
                                                <h3>{p.affair}</h3>
                                                <p>{p.content}</p>
                                                {p.imgPath.length > 0 && p.imgPath[0] !== "VACIO" &&
                                                    <div className="imgPostContainer">
                                                        {
                                                            p.imgPath.map((i) => {
                                                                return <div className="sigleImgPost" key={i}><img src={`http://localhost:8080/file/${i}`} alt="" /></div>
                                                            })
                                                        }
                                                    </div>
                                                }
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                            <div id="Javascript" className="tab-pane fade">
                                <h3>Posts Favoritos</h3>
                                <div id="divPosts">
                                    {likedPostsUser.length < 1 &&
                                        <p id="noHayPost">No hay ninguna publicación favorita</p>
                                    }
                                    <br />
                                    {postLoading === false && likedPostsUser !== undefined &&
                                        user.likedPosts.map((p) => {
                                            console.log(comprobarUserLiked(p));
                                            var bool = comprobarUserLiked(p);
                                            return <div className="post" id={"postTodos2" + p.id} key={p.id}>
                                                <button className="userWhoPost" onClick={() => navigateTo(p.userWhoPost.userName)}>
                                                    <img src={`http://localhost:8080/file/${p.userWhoPost.imgPath}`} alt="" />
                                                    <p>{p.userWhoPost.userName}</p>
                                                    {p.userWhoPost.verified &&
                                                        <FontAwesomeIcon icon={faCheckCircle} color="#2590EB" size="1x" className="verificadoUser" />
                                                    }
                                                </button>
                                                {localStorage.getItem("loggedUser") === userName &&
                                                    <FontAwesomeIcon icon={faHeart} className="likePostButton" style={{ color: "red" }} onClick={() => { document.getElementById("postTodos2" + p.id).style.display = "none"; likeAPost(p); document.getElementById("corazon" + p.id).style.color = "white"; var n = document.getElementById("numLike" + p.id); n.innerHTML = parseInt(parseInt(n.innerHTML) - 1) }} size={"2x"} id={"corazon" + p.id} color="red" />
                                                }
                                                {localStorage.getItem("loggedUser") !== userName &&
                                                    <span className="numberUsersWhoLiked" id={"numLike" + p.id}>{p.usersWhoLiked}</span>
                                                }
                                                {localStorage.getItem("loggedUser") !== userName &&
                                                    <FontAwesomeIcon icon={faHeart} className="likePostButton" style={bool ? { color: "red" } : { color: "white" }} onClick={() => { likeAPost(p); cambiarIcono(p.id); var l = document.getElementById("numLike" + p.id); var c = document.getElementById("corazon" + p.id); c.style.color !== "white" ? l.innerHTML = parseInt(parseInt(l.innerHTML) + 1) : l.innerHTML = parseInt(parseInt(l.innerHTML) - 1); likedPostsUser.filter(po => po.id === p.id).length > 0 ? setLikedPostsUser(likedPostsUser) : setLikedPostsUser(likedPostsUser.push(p)) }} size={"2x"} id={"corazon" + p.id} color="red" />
                                                }
                                                <h3>{p.affair}</h3>
                                                <p>{p.content}</p>
                                                {p.imgPath.length > 0 && p.imgPath[0] !== "VACIO" &&
                                                    <div className="imgPostContainer">
                                                        {
                                                            p.imgPath.map((i) => {
                                                                return <div className="sigleImgPost" key={i}><img src={`http://localhost:8080/file/${i}`} alt="" /></div>
                                                            })
                                                        }
                                                    </div>
                                                }
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                            <div id="Bootstrap" className="tab-pane fade">
                                <div id="buscador">
                                    <h3>Todos los followers</h3>
                                </div>
                                <div id="divPosts">
                                    {user.followers.length < 1 &&
                                        <p id="noHayPost">No existen usuarios</p>
                                    }
                                    {loadingUsers === false && user.followers !== undefined &&
                                        user.followers.map((u) => {
                                            var bool2 = u.followers >= 1;
                                            return <div key={u.id}>
                                                <div className="user" id={"userTodos" + u.id}>
                                                    <img src={`http://localhost:8080/file/${u.imgPath}`} alt="" />
                                                    <div className="datasUsuarios">
                                                        <h2>{u.userName} <span><FontAwesomeIcon icon={faCheckCircle} color="white" size="xs" id={"verified" + u.id} style={bool2 ? { visibility: "visible" } : { visibility: "hidden" }} className="verificadoUser" /></span></h2>
                                                        <p>{u.fullName}</p>
                                                        <p><b>Followers: <span id={"foll" + u.id}>{u.followers > 999 ? u.followers > 1000000 ? (u.followers / 1000000).toFixed(1) + "M" : u.followers / 1000 : u.followers}</span>&nbsp;&nbsp;&nbsp;&nbsp;Posts: {u.countOfPosts}</b></p>
                                                    </div>
                                                </div>
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                            <div id="Jquery" className="tab-pane fade">
                                <div id="buscador">
                                    <h3>Todos los follows</h3>
                                </div>
                                <div id="divPosts">
                                    {user.follows.length === 0 &&
                                        <p id="noHayPost">No existen usuarios</p>
                                    }
                                    {loadingUsers === false && user.follows !== undefined &&
                                        user.follows.map((u) => {
                                            var bool2 = u.followers >= 1;
                                            return <div key={u.id}>
                                                <div className="user" id={"userTodosFo" + u.id}>
                                                    <img src={`http://localhost:8080/file/${u.imgPath}`} alt="" />
                                                    <div className="datasUsuarios">
                                                        <h2>{u.userName} <span><FontAwesomeIcon icon={faCheckCircle} color="white" size="xs" id={"verified" + u.id} style={bool2 ? { visibility: "visible" } : { visibility: "hidden" }} className="verificadoUser" /></span></h2>
                                                        <p>{u.fullName}</p>
                                                        <p><b>Followers: <span id={"foll" + u.id}>{u.followers > 999 ? u.followers > 1000000 ? (u.followers / 1000000).toFixed(1) + "M" : u.followers / 1000 : u.followers}</span>&nbsp;&nbsp;&nbsp;&nbsp;Posts: {u.countOfPosts}</b></p>
                                                    </div>
                                                </div>
                                            </div>
                                        })
                                    }
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        );
    }
}

export default Profile;