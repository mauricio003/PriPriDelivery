import React from 'react';

/**
 * Reusable KPI card component.
 * Props:
 *  - Icon: React component for the icon (e.g., DollarSign)
 *  - label: string – description of the metric
 *  - value: string or number – formatted metric value
 *  - bgClass: Tailwind background color class (e.g., 'bg-red-50')
 *  - textClass: Tailwind text color class (e.g., 'text-red-600')
 */
export default function KpiCard({ Icon, label, value, bgClass = 'bg-gray-50', textClass = 'text-gray-600' }) {
  return (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center">
      <div className={`p-3 rounded-xl ${bgClass} ${textClass} mr-4`}>
        {Icon && <Icon className="w-6 h-6" />}
      </div>
      <div>
        <p className="text-sm font-medium text-gray-500">{label}</p>
        <p className="text-2xl font-bold text-gray-900">{value}</p>
      </div>
    </div>
  );
}
