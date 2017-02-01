using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(cecs491.Startup))]
namespace cecs491
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
