import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Q2 {
    private static final String STRING_404 = "404";
    private static final String STRING_WILDCARD = "X";
    private static final String STRING_PATH_SEPARATOR = "/";

    private static final class RouteDeconstructionWrapper {
        private Map<String, RouteDeconstructionWrapper> children = new HashMap<>();
        private String currentValue;
        private String method;

        RouteDeconstructionWrapper(String component) {
            this.currentValue = component;
        }

        void addChild(RouteDeconstructionWrapper child) {
            this.children.put(child.currentValue, child);
        }
    }

    /*
     * The solution is done in two parts;
     *  1) given the configuration, construct a tree of different paths
     *
     *  2) Given a path set, attempt to parse them against the tree above, and
     *     at the end, return the dispatch methods that map to (if any) each path.
     *     There are two parts to parsing ... the first is to attempt static
     *     and wildcard routes; the second part is favoring the static route
     *     first, if one exists, when matching against the dispatch method
     *
     *
     * Runtime complexity;
     *  1) For the configuration tree (T), we need to create `n` nodes that we parse off
     *     of configuration file.  So `n` is the number of different paths in the tree.
     *     Plus parsing of the path, which is at worst the depth of the tree.
     *     Therefore, runtime is O(n)+O(depth(T))
     *
     *  2) Matching is also similar to 1) above, since we still have to traverse the
     *     entire tree (worst case), and we still have to parse the input
     *
     *
     * Space complexity;
     *  1) For the configuration tree, we need to create `n` nodes that we parse off
     *     of configuration file.
     *
     *  2) For each path, we split it to different components (by the `/` token).  The
     *     worst case space complexity is the depth of the tree.
     *
     */
    private static List<String> routeAll(List<Route> routes /* config */, List<String> paths /* input */) {
        List<String> endpoints = new ArrayList<>();
 
        if (routes != null && !routes.isEmpty() && paths != null && !paths.isEmpty()) {

            final RouteDeconstructionWrapper routeTree = constructRoutes(routes);
            //printRouteWrapper(routeTree, "-");

            // now, let's check to see if a path matches
            for (final String path : paths) {
                String lookupPath = getRouteFromPath(routeTree, path);
                endpoints.add(lookupPath == null ? STRING_404 : lookupPath);
            }
        }

        return endpoints;
    }

    private static void printRouteWrapper(final RouteDeconstructionWrapper routeTree, String padding) {
        System.out.println(padding + "" + routeTree.currentValue + "(" + routeTree.method + ")");
        if (!routeTree.children.isEmpty()) {
            padding = padding + "" + padding;
            for (Map.Entry<String, RouteDeconstructionWrapper> entry : routeTree.children.entrySet()) {
                printRouteWrapper(entry.getValue(), padding);
            }
        }
    }

    private static String getRouteFromPath(final RouteDeconstructionWrapper routeTree, final String path) {
        if (path != null && path.length() > 0 && path.trim().length() > 0 && path.contains(STRING_PATH_SEPARATOR)) {
            final String[] components = path.split(STRING_PATH_SEPARATOR);

            return bfsOnTheTree(routeTree, components, 0);
        }
        return null;
    }

    private static String bfsOnTheTree(RouteDeconstructionWrapper currentLocationInTree, String[] components, int indexOfComponent) {
        if (currentLocationInTree != null) {

            // if we're at the end of the path component, just return the method/dispatch we have so far
            if (components == null || indexOfComponent == components.length) {
                return currentLocationInTree.method;
            }

			
            final String component = components[indexOfComponent];

            // base case - if we are at the root of the tree/path (this is just an empty string ""), go one level down
            if (component != null && (component.isEmpty() || component.trim().isEmpty())) {
                return bfsOnTheTree(currentLocationInTree, components, ++indexOfComponent);
            }

            // get both the component and wildcard endpoints
            final RouteDeconstructionWrapper temp = currentLocationInTree.children.get(component);
            final RouteDeconstructionWrapper tempWildcard = currentLocationInTree.children.get(STRING_WILDCARD);

            // get their dispatch names
            final String tempResult = bfsOnTheTree(temp, components, indexOfComponent + 1); //for static
            final String tempWildcardResult = bfsOnTheTree(tempWildcard, components, indexOfComponent + 1); // for wildcard


            //
            // since the requirement is
            // "If multiple path patterns match, you should prefer static patterns to patterns with wildcards"
            // we first check the static one to ensure there is a method dispatch ... if there is, return it; otherwise
            // see if the wildcard has anything
            //
            if (tempResult != null) {
                return tempResult;
            }

            if (tempWildcardResult != null) {
                return tempWildcardResult;
            }
        }

        return null;
    }

    private static RouteDeconstructionWrapper constructRoutes(List<Route> routes) {
        final RouteDeconstructionWrapper routeTree = new RouteDeconstructionWrapper(null);
        RouteDeconstructionWrapper currentRouteNode = routeTree;

        for (final Route route : routes) {
            final String path = route.path;
            if (path != null && path.length() > 0 && path.trim().length() > 0 && path.contains(STRING_PATH_SEPARATOR)) {
                final String[] components = path.split(STRING_PATH_SEPARATOR);

                for (final String component : components) {
                    if (component.isEmpty() && components.length == 1) { // base case - setting the root element!
                        currentRouteNode.currentValue = route.endpoint;
                    } else if (!component.isEmpty()) {
                        RouteDeconstructionWrapper leaf = currentRouteNode.children.get(component);

                        if (leaf == null) { // this component in path does not exist, create and add it
                            final RouteDeconstructionWrapper tempRouteDeconstructionWrapper = new RouteDeconstructionWrapper(component);
                            currentRouteNode.addChild(tempRouteDeconstructionWrapper);
                            currentRouteNode = tempRouteDeconstructionWrapper;
                        } else { // we already have this path, thus far ... use it for the next iteration
                            currentRouteNode = leaf;
                        }
                    }
                }

                // we're at the end, so set the method down the tree where we are right now
                currentRouteNode.method = route.endpoint;

                // reset to the root of the tree, so that it's ready for the next path configuration
                currentRouteNode = routeTree;
            }
        }

        return routeTree;
    }

    static class Route {
        String path;
        String endpoint;

        public Route(String path, String endpoint) {
            this.path = path;
            this.endpoint = endpoint;
        }
    }

    private static List<Route> getRoutes(final InputStream is) throws IOException {
        final List<Route> routes = new ArrayList<Route>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            final String[] tokenizedLine = line.split(" ");
            routes.add(new Q2.Route(tokenizedLine[0], tokenizedLine[1]));
        }
        return routes;
    }

    private static List<String> getPaths(final InputStream is) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final List<String> paths = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null && line.length() != 0) {
            paths.add(line);
        }
        return paths;
    }

    public static void main(final String... args) throws IOException {
        final List<Route> routes = Q2.getRoutes(new FileInputStream(args[0]));
        final List<String> paths = Q2.getPaths(System.in);

        for (final String endpoint : Q2.routeAll(routes, paths)) {
            System.out.println(endpoint);
        }
    }
}