import React from 'react';
import { NavLink } from 'react-router-dom';
import { Home, BarChart2, Store, MapPin } from 'lucide-react';

export default function Sidebar({ isOpen, toggle }) {
  return (
    <div className={`fixed inset-y-0 left-0 z-30 w-64 bg-white border-r border-gray-200 transform ${isOpen ? 'translate-x-0' : '-translate-x-full'} transition-transform duration-300 ease-in-out lg:relative lg:translate-x-0`}>
      {/* Close button for mobile */}
      <div className="flex items-center justify-between p-4 lg:hidden">
        <span className="text-lg font-semibold">Admin</span>
        <button onClick={toggle} className="text-gray-600 hover:text-gray-800">
          ✕
        </button>
      </div>
      <nav className="mt-4">
        <NavLink to="/home" className={({ isActive }) => `flex items-center px-4 py-2 text-sm ${isActive ? 'bg-red-50 text-red-600' : 'text-gray-600 hover:bg-gray-100'}`}>
          <Home className="w-5 h-5 mr-2" /> Home
        </NavLink>
        <NavLink to="/relatorios" className={({ isActive }) => `flex items-center px-4 py-2 text-sm ${isActive ? 'bg-red-50 text-red-600' : 'text-gray-600 hover:bg-gray-100'}`}>
          <BarChart2 className="w-5 h-5 mr-2" /> Relatórios
        </NavLink>
        <NavLink to="/restaurante" className={({ isActive }) => `flex items-center px-4 py-2 text-sm ${isActive ? 'bg-red-50 text-red-600' : 'text-gray-600 hover:bg-gray-100'}`}>
          <Store className="w-5 h-5 mr-2" /> Restaurantes
        </NavLink>
        <NavLink to="/endereco" className={({ isActive }) => `flex items-center px-4 py-2 text-sm ${isActive ? 'bg-red-50 text-red-600' : 'text-gray-600 hover:bg-gray-100'}`}>
          <MapPin className="w-5 h-5 mr-2" /> Endereços
        </NavLink>
      </nav>
    </div>
  );
}
