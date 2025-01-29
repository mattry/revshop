import AuthForm from "./auth-forms/AuthForm";
import { useUser } from './UserContext';

const LandingPage = () => {

    const { user } = useUser();

    if (!user){
        return(
            <AuthForm />
        )
    }

    return(
        <>
        <p>Hello, {user.username}</p>
        </>
    )

}

export default LandingPage;