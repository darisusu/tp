<head-bottom>
  <link rel="stylesheet" href="{{baseUrl}}/stylesheets/main.css">
</head-bottom>

<header sticky>
  <navbar type="dark">
    <a slot="brand" href="{{baseUrl}}/index.html" title="Home" class="navbar-brand">FitBook</a>
    <li><a href="{{baseUrl}}/index.html" class="nav-link">Home</a></li>
    <li><a href="{{baseUrl}}/UserGuide.html" class="nav-link">User Guide</a></li>
    <li><a href="{{baseUrl}}/DeveloperGuide.html" class="nav-link">Developer Guide</a></li>
    <li><a href="{{baseUrl}}/AboutUs.html" class="nav-link">About Us</a></li>
    <li><a href="https://github.com/AY2526S1-CS2103T-F09-4/tp" target="_blank" class="nav-link"><md>:fab-github:</md></a>
    </li>
    <li slot="right">
      <form class="navbar-form">
        <searchbar :data="searchData" placeholder="Search" :on-hit="searchCallback" menu-align-right></searchbar>
      </form>
    </li>
  </navbar>
</header>

<div id="flex-body">
  <nav id="site-nav">
    <div class="site-nav-top">
      <div class="fw-bold mb-2" style="font-size: 1.25rem;">Site Map</div>
    </div>
    <div class="nav-component slim-scroll">
      <site-nav>
* [Home]({{ baseUrl }}/index.html)
* [User Guide]({{ baseUrl }}/UserGuide.html) :expanded:
  * [Quick Start]({{ baseUrl }}/UserGuide.html#quick-start)
  * [Features]({{ baseUrl }}/UserGuide.html#features)
  * [FAQ]({{ baseUrl }}/UserGuide.html#faq)
  * [Command Summary]({{ baseUrl }}/UserGuide.html#faq)
* [Developer Guide]({{ baseUrl }}/DeveloperGuide.html) :expanded:
  * [Acknowledgements]({{ baseUrl }}/DeveloperGuide.html#acknowledgements)
  * [Setting Up]({{ baseUrl }}/DeveloperGuide.html#setting-up-getting-started)
  * [Design]({{ baseUrl }}/DeveloperGuide.html#design)
    * [Architecture]({{ baseUrl }}/DeveloperGuide.html#architecture)
    * [UI component]({{ baseUrl }}/DeveloperGuide.html#ui-component)
    * [Logic component]({{ baseUrl }}/DeveloperGuide.html#logic-component)
    * [Model component]({{ baseUrl }}/DeveloperGuide.html#model-component)
    * [Storage component]({{ baseUrl }}/DeveloperGuide.html#storage-component)
    * [Common classes]({{ baseUrl }}/DeveloperGuide.html#common-classes)
  * [Implementation]({{ baseUrl }}/DeveloperGuide.html#implementation)
    * [Sort by Paid Status feature]({{ baseUrl }}/DeveloperGuide.html#sort-by-paid-status-feature)
    * [Proposed Undo/redo feature]({{ baseUrl }}/DeveloperGuide.html#proposed-undoredo-feature)
  * [Documentation, logging, testing, configuration, dev-ops]({{ baseUrl }}/DeveloperGuide.html#documentation-logging-testing-configuration-dev-ops)
  * [Appendix: Requirements]({{ baseUrl }}/DeveloperGuide.html#appendix-requirements)
    * [Product scope]({{ baseUrl }}/DeveloperGuide.html#product-scope)
    * [User stories]({{ baseUrl }}/DeveloperGuide.html#user-stories)
    * [Use cases]({{ baseUrl }}/DeveloperGuide.html#use-cases)
    * [Non-Functional Requirements]({{ baseUrl }}/DeveloperGuide.html#non-functional-requirements)
    * [Glossary]({{ baseUrl }}/DeveloperGuide.html#glossary)
  * [Appendix: Instructions for manual testing]({{ baseUrl }}/DeveloperGuide.html#appendix-instructions-for-manual-testing)
* [About Us]({{ baseUrl }}/AboutUs.html)
      </site-nav>
    </div>
  </nav>
  <div id="content-wrapper">
    {{ content }}
  </div>
  <nav id="page-nav">
    <div class="nav-component slim-scroll">
      <page-nav />
    </div>
  </nav>
  <scroll-top-button></scroll-top-button>
</div>

<footer>
  <!-- Support MarkBind by including a link to us on your landing page! -->
  <div class="text-center">
    <small>[<md>**Powered by**</md> <img src="https://markbind.org/favicon.ico" width="30"> {{MarkBind}}, generated on {{timestamp}}]</small>
  </div>
</footer>
