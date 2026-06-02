import React from 'react';
import { Search, Bell, User } from 'lucide-react';

export default function TopBar() {
  return (
    <nav className="bg-white shadow-sm sticky top-0 z-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        {/* Search */}
        <div className="flex items-center w-full max-w-md">
          <div className="relative w-full">
            <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar..."
              className="w-full pl-10 pr-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            />
          </div>
        </div>
        {/* Icons */}
        <div className="flex items-center space-x-4">
          <button className="text-gray-600 hover:text-gray-800 transition-colors">
            <Bell className="w-5 h-5" />
          </button>
          <button className="flex items-center space-x-2 text-gray-600 hover:text-gray-800 transition-colors">
            <User className="w-5 h-5" />
            <span className="hidden sm:inline-block">Admin</span>
          </button>
        </div>
      </div>
    </nav>
  );
}
