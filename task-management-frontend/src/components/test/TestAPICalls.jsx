import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllTasks } from "../../services/task";

export default function TestAPICalls() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    let isMounted = true;
    const controller = new AbortController();

    const checkAuthAndFetch = async () => {
      try {
        // 1. Check for token first
        if (!localStorage.getItem('token')) {
          navigate('/login', { replace: true });
          return;
        }

        // 2. Fetch data with cancellation support
        const tasks = await getAllTasks({ signal: controller.signal });
        if (isMounted) {
          setTasks(tasks);
          setLoading(false);
        }
      } catch (error) {
        if (isMounted) {
          setLoading(false);
          if (error.name !== 'CanceledError') {
            setError(error.message);
            console.error('API Error:', error);
          }
        }
      }
    };

    checkAuthAndFetch();

    // 3. Cleanup function to cancel pending requests
    return () => {
      isMounted = false;
      controller.abort();
    };
  }, [navigate]); // 4. Add required dependencies

  if (loading) return <div>Loading tasks...</div>;
  if (error) return <div>Error loading tasks: {error}</div>;

  return (
    <div>
      <h2>Tasks</h2>
      {/* Render tasks here */}
    </div>
  );
}