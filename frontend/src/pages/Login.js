import {Box, Button, Container, Paper, TextField, ThemeProvider} from "@mui/material";
import {useEffect, useState} from "react";
import "../styles/Login.css";

function Login()
{
    const paperStyle = {padding: '50px 20px', width: 600, margin:'20px auto'}

    const[email, setEmail] = useState('');
    const[password, setPassword] = useState('');
    const[name, setName] = useState('');

    function login(user)
    {
        fetch("http://localhost:8080/login",
            {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(user)
            }).then((res2)=>
        {
            const reader = res2.body.getReader();
            reader.read().then(({value})=>
            {
                const str = new TextDecoder("utf-8").decode(value);
                if(str == null)
                {
                    return;
                }
                localStorage.setItem('jwtToken', str)
                console.log("Token is: ", str)
            });
        })
    }
    useEffect(()=> {
        document.getElementById('login').style.display = 'block';
    },[])

    const handleClick=(e)=>
    {
        if(e.target.id === 'loginbutton')
        {
            e.preventDefault();
            const user = {email: email,password: password};
            login(user);
        }
    }

    function LoginDiv() {
        return (
            <div>
                <Box className="center">
                    <div id="login">
                        <Paper elevation={3} className="paper_container">
                            <h1 color="primary" className="center"> Login</h1>
                            <Box component="form" sx={{'& > :not(style)': {m: 1},}} noValidate autoComplete="off">
                                {/* TextField is used to take input from the user, When it is changed, the name is updated through setName */}
                                <TextField label="Email" variant="outlined" fullWidth value={email}
                                           onChange={(e) => setEmail(e.target.value)}/>
                                <TextField label="Password" variant="outlined" type="password" fullWidth
                                           value={password} onChange={(e) => setPassword(e.target.value)}/>
                            </Box>
                            {/* Button is used to submit the form When it is clicked, handleClick is called */}
                            <div className="center">
                                <Button id="loginbutton" variant="contained" color="primary" style={{margin: '2%'}}
                                        onClick={handleClick}>Log In</Button>
                                <Button id="swap" color="primary" onClick={handleClick}>Don't have an account?</Button>
                            </div>
                        </Paper>
                    </div>
                </Box>
            </div>
        )

    }
    return(
        <Paper style={{height: "100vh"}}>
            {LoginDiv()}
        </Paper>
    );
}

export default Login;