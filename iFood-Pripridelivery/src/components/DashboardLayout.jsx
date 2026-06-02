import React from 'react';
import Sidebar from './Sidebar';

export default function DashboardLayout({ children }) {
  // State to control sidebar visibility on mobile
  const [sidebarOpen, setSidebarOpen] = React.useState(false);

  const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

  return (
    <div className="flex min-h-screen bg-gray-50">
      {/* Sidebar */}
      <Sidebar isOpen={sidebarOpen} toggle={toggleSidebar} />

      {/* Main content area */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Mobile top bar with menu button */}
        <div className="lg:hidden flex items-center p-4 bg-white shadow">
          <button onClick={toggleSidebar} className="text-gray-600 hover:text-gray-800">
            ☰
          </button>
          <h1 className="ml-4 text-lg font-semibold text-gray-900">Admin</h1>
        </div>
        <main className="flex-1 overflow-y-auto p-4">
          {children}
        </main>
      </div>
    </div>
  );
}
