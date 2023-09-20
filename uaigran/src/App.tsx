import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "./Pages/Login/Login";
import Registration from "./Pages/Registration/Registration";
import Home from "./Pages/Home/Home";
import Friends from "./Pages/Friends/Friends";
import Profile from "./Pages/Profile/Profile";
import AddFriends from "./Pages/AddFriends/AddFriends";
import EditProfile from "./Pages/EditProfile/EditProfile";
import PostDetail from "./Pages/PostDetail/PostDetail";
const router = createBrowserRouter([
  {
    path: "/",
    element: <Login/>,
  },
  {
    path: "/registration",
    element: <Registration/>
  },
  {
    path: "/home",
    element: <Home/>
  },
  {
    path: "/friends",
    element: <Friends/>
  },
  {
    path: "/profile",
    element: <Profile/>
  },
  {
    path: "/addFriend",
    element: <AddFriends/>
  },
  {
    path: "/editProfile",
    element: <EditProfile />
  },
  {
    path: "/postDetail/:postId",
    element: <PostDetail />
  }
]);

function App() {
  return <RouterProvider router={router}/>;
}

export default App;
