package com.InfluenceMaximization;

import java.util.Scanner;



public class MTS {

	public static void main(String[] args) {
		
			
				Scanner sc = new Scanner(System.in);
				
				System.out.println("Enter the no of vertices in Network");    //input no of vertex in network
				
				int n = sc.nextInt();
				
				if(n > 0)
				{
					int addj[][] = new int[n+1][n+1];
				
					//Take Input Graph in adjacenecy matrix form...
				
					System.out.println("Enter the adjacency matrix");
				
					for(int i = 1; i <= n; i++)
					{
						for(int j = 1; j <= n; j++)
						{
							addj[i][j] = sc.nextInt();
						}
					}
				
					//declaration of variable sets....
				
					int S[] = new int[n+1];
					int L[]	= new int[n+1];
					int U[] = new int[n+1];
					int V[] = new int[n+1];
					int in_degree_set [][] = new int[n+1][n+1];
					int out_degree_set[][] = new int [n+1][n+1];
					int indeg [] = new int [n+1];
					int outdeg [] = new int[n+1];
					int k [] = new int[n+1];
					boolean flag1 = false;
					boolean flag2 = false;
					int selected_case = 0;
					int selected_vertex = 0;
					
					
					//Intialization...
					for( int i = 1; i <= n; i++)
					{
						S[i] = 0;
					    L[i] = 0;
					}
						
			 		for(int i = 1; i <= n; i++)
						V[i] = i;
					
					U = V;
					
					//Calculate in-degree and out-degree neighbors sets
					for(int i = 1; i <= n; i++)
					{
						int in_count = 0;  //for indegree
						int out_count = 0; //for outdegree
			
						for(int j = 1; j <= n; j++)
						{
							if(addj[i][j] != 0)
								out_degree_set[i][++out_count] = j;
							
							if(addj[j][i] != 0)
								in_degree_set[i][++in_count] = j;
						}
						
						indeg[i] = in_count;
						k[i] = indeg[i];
						outdeg[i] = out_count;
					}
					
					System.out.println("-------------------------Intermediate Result--------------------------------");
					
					// Begin MTS() Algorithm...
					while(!is_empty(U))
					{
						// case 1...
						for(int i = 1 ; i <=n ; i++)
						{
							if( exists(U,i) && k[i] == 0 )
							{
								selected_case = 1;
								selected_vertex = i;
								//System.out.println(" k[v] == 0 "+ " "+ i);
								for(int j = 1; j <= non_zero_count(in_degree_set[i]); j++)
								{
									delete_element(out_degree_set[j],i);	
								}
							
								for(int j = 1 ; j <= non_zero_count(out_degree_set[i]); j++)
								{
									
									k[out_degree_set[i][j]] = max(k[out_degree_set[i][j]]-1,0);
										if(!exists(L,i))
											delete_element(in_degree_set[out_degree_set[i][j]],i);
								}	
								delete_element(U,i);
								flag1 = true;
								break;
							}
						}
						
						// case 2...
						if(flag1 == false)
						{
							for( int i = 1 ; i <= n; i++ )
							{
								if( exists(U,i) && !exists(L,i) && non_zero_count(in_degree_set[i])<k[i] )
								{
									//System.out.println("case 2" +" "+ i);
									selected_case = 2;
									selected_vertex = i;
									add(S, i);
										
									for( int j = 1; j <= non_zero_count(in_degree_set[i]); j++ )
										delete_element(out_degree_set[in_degree_set[i][j]],i);
								
								    for( int j = 1; j <= non_zero_count(out_degree_set[i]); j++ )
									{
										k[out_degree_set[i][j]] = k[out_degree_set[i][j]]-1;
										delete_element(in_degree_set[out_degree_set[i][j]],i);
									}
								
									delete_element(U,i);
									flag2 = true;
									break;
								}
							}
							
							//case 3...
							if(flag2 == false)
							{
								double max = 0.0;
								selected_case = 3;
								for( int i = 1 ; i <= n ; i++ )
								{	
									if( exists(U,i) && !exists(L,i) )
									{
										int mul = (non_zero_count(in_degree_set[i])*(non_zero_count(in_degree_set[i])+1));
										double temp = (double)k[i]/mul;
										
										if(temp > max)
										{
											max = temp;
											selected_vertex = i;
										}
									}
								}
								
								add(L,selected_vertex);
								
								for(int j = 1 ; j <= non_zero_count(out_degree_set[selected_vertex]); j++)
								{
									delete_element(in_degree_set[out_degree_set[selected_vertex][j]],selected_vertex);
								}
							
							}
							flag2 = false;
						}
						flag1 = false;
						
						//Print Result...		
						System.out.print("v"+selected_vertex+ "   " +selected_case+ "   "+"{ ");
						for(int i = 1; i <= non_zero_count(U); i++)
									System.out.print("v"+U[i]+" ");
						System.out.print("}"+"  ");
						
						for(int i = 1; i <= ((n-non_zero_count(U))*3); i++ )
							System.out.print(" ");
						
						System.out.print("{ ");
						for(int i = 1; i <= non_zero_count(S); i++)
									System.out.print("v"+S[i]+" ");
						System.out.print("}");		
								
						System.out.println();		
						
						selected_case = 0;
						selected_vertex = 0;
					}
					System.out.println("----------------------------------------------------------------------------");
					System.out.println();
					System.out.println();
					System.out.println("-------------------------------Final Result---------------------------------");
					System.out.print("Target set of Given social Network   { ");
					for(int i = 1; i <= non_zero_count(S); i++ )
						System.out.print("v"+S[i]+" ");
					System.out.println("}");
					System.out.println("----------------------------------------------------------------------------");
				}
				else
				{
					System.out.println("Re-run program and Enter the non zero number of vertices as input....");
					System.exit(0);
				}
			}

			static // maximum function
			int max(int a, int b)
			{
				if(a>=b)
					return a;
				else
					return b;
			}
			
			// is_empty function 
			static boolean is_empty(int arr[])
			{
				int count = 0;
				int size = arr.length;
				for(int i = 1 ; i <= size-1; i++)
				{
					if(arr[i] != 0)
						count++;
				}
				
				if(count == 0)
					return true;
				else
					return false;
			}
			
			// non zero count function
			static int non_zero_count(int arr[])
			{
				int size = arr.length;
				int count = 0;
				for(int i = 1; i <= size-1; i++)
				{
					if(arr[i]!=  0)
						count++;
				}
				
				return count;
			}
			
			static // element deletion function
			void delete_element(int arr[] , int x)
			{
				int size = arr.length;
				int loc = 0;
				
				for(int i = 1; i <= size-1; i++)
				{
					if(arr[i] == x)
						loc = i;
				}
				
				for(int i = loc+1; i <= size-1; i++)
				{
					arr[i-1] = arr[i];
				}
				arr[size-1] = 0;
			}
			
			// element presence function
			static boolean exists(int arr[], int x)
			{
				int size = arr.length;
				
				boolean flag = false;
				
				for(int i = 1; i <= size-1; i++)
				{
					if(arr[i] == x)
					{
						flag = true;
						break;		
					}
				}
				return flag;
			}
			
			// element add function
		    static void add(int arr[] , int x)
			{
				int size = arr.length;
				int loc  = non_zero_count(arr);
				if(loc <= size-1)
					arr [loc+1] = x; 
			}
			
		}
	


