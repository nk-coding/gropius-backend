import React from 'react';
import DocSidebar from '@theme-original/DocSidebar';
import BrowserOnly from '@docusaurus/BrowserOnly';

export default function DocSidebarBrowser(props) {
    return (
        <BrowserOnly
            fallback={<div>Sidebar...</div>}>
            {() => {
                return <DocSidebar {...props} />
            }}
        </BrowserOnly>
    );
}
