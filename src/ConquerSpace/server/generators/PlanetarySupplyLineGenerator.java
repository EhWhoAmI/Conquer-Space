/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.server.generators;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.logistics.SupplyManager;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetarySupplyLineGenerator {

    public void generate(GameState gameState, Planet p) {
        //Kruskal's Spanning Tree Algorithm
        //Create set of edges
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<ObjectReference> alreadyConnected = new ArrayList<>();
        BidiMap<Integer, ObjectReference> cityIds = new DualHashBidiMap<>();
        int i = 0;
        for (City c : p.getCities()) {
            cityIds.put(i, c.getReference());
            i++;
        }

        for (City c : p.getCities()) {
            //Connect cities
            for (City c2 : p.getCities()) {
                if (c != c2) {
                    //Check if connected
                    if (!alreadyConnected.contains(c2.getReference())) {
                        edges.add(new Edge(cityIds.getKey(c.getReference()), cityIds.getKey(c2.getReference()), c.getInitialPoint().distance(c2.getInitialPoint())));
                    }
                }
            }
            alreadyConnected.add(c.getReference());
        }

        //Sort by distance
        Collections.sort(edges);

        Graph g = new Graph(p.getCities().size(), edges.size());
        for (Edge edge : edges) {
            g.edges.add(edge);
        }
        g.KruskalMST();
        //Do stuff...
        for (int k = 0; k < g.resultCount; k++) {
            //Connect cities
            Edge res = g.result[k];
            ObjectReference src = cityIds.get(res.src);
            ObjectReference dest = cityIds.get(res.dest);
            SupplyManager.connectSegments(gameState, src, dest);
        }
    }

    class Edge implements Comparable<Edge> {

        Integer src, dest;
        double weight;

        public Edge(Integer src, Integer dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        // Comparator function used for sorting edges  
        // based on their weight 
        public int compareTo(Edge compareEdge) {
            return Double.compare(weight, compareEdge.weight);
        }
    };

    class Graph {
        // A class to represent a graph edge 

        // A class to represent a subset for union-find 
        class subset {

            int parent, rank;
        };

        int V, E;    // V-> no. of vertices & E->no.of edges 
        int resultCount = 0;
        ArrayList<Edge> edges; // collection of all edges 
        Edge result[];

        // Creates a graph with V vertices and E edges 
        Graph(int v, int e) {
            V = v;
            E = e;
            edges = new ArrayList<>();
            result = new Edge[V];  // Tnis will store the resultant MST 
        }

        // A utility function to find set of an element i 
        // (uses path compression technique) 
        int find(subset subsets[], int i) {
            // find root and make root as parent of i (path compression) 
            if (subsets[i].parent != i) {
                subsets[i].parent = find(subsets, subsets[i].parent);
            }

            return subsets[i].parent;
        }

        // A function that does union of two sets of x and y 
        // (uses union by rank) 
        void Union(subset subsets[], int x, int y) {
            int xroot = find(subsets, x);
            int yroot = find(subsets, y);

            // Attach smaller rank tree under root of high rank tree 
            // (Union by Rank) 
            if (subsets[xroot].rank < subsets[yroot].rank) {
                subsets[xroot].parent = yroot;
            } else if (subsets[xroot].rank > subsets[yroot].rank) {
                subsets[yroot].parent = xroot;
            } // If ranks are same, then make one as root and increment 
            // its rank by one 
            else {
                subsets[yroot].parent = xroot;
                subsets[xroot].rank++;
            }
        }

        // The main function to construct MST using Kruskal's algorithm 
        void KruskalMST() {
            int e = 0;  // An index variable, used for result[] 
            int i = 0;  // An index variable, used for sorted edges 

            // Step 1:  Sort all the edges in non-decreasing order of their 
            // weight.  If we are not allowed to change the given graph, we 
            // can create a copy of array of edges 
            Collections.sort(edges);

            // Allocate memory for creating V ssubsets 
            subset subsets[] = new subset[V];
            for (i = 0; i < V; ++i) {
                subsets[i] = new subset();
            }

            // Create V subsets with single elements 
            for (int v = 0; v < V; ++v) {
                subsets[v].parent = v;
                subsets[v].rank = 0;
            }

            i = 0;  // Index used to pick next edge 

            // Number of edges to be taken is equal to V-1 
            while (e < V - 1) {
                // Step 2: Pick the smallest edge. And increment  
                // the index for next iteration 
                Edge next_edge = edges.get(i++);

                int x = find(subsets, next_edge.src);
                int y = find(subsets, next_edge.dest);

                // If including this edge does't cause cycle, 
                // include it in result and increment the index  
                // of result for next edge 
                if (x != y) {
                    result[e++] = next_edge;
                    Union(subsets, x, y);
                }
                // Else discard the next_edge 
            }
            resultCount = e;
        }
    }
}
