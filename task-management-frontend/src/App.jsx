import { Routes, Route, Navigate } from 'react-router-dom';
import TestAPICalls from './components/test/TestAPICalls';
import Login from './pages/Login'; // Create this component

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/tasks" element={<TestAPICalls />} />
      <Route path="/" element={<Navigate to="/tasks" replace />} />
    </Routes>
  );
}

export default App;